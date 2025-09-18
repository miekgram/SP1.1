package app.Services;


import app.config.HibernateConfig;
import app.dao.Dao;
import app.dtos.*;
import app.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MovieService  {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static Dao dao = new Dao(emf);



    private static final String API_KEY = System.getenv("api_key");  // Replace with your TMDb API Key
    private static final String BASE_URL_DISCOVER = "https://api.themoviedb.org/3/discover/movie";



public static List<Movie> getAllMovies (){
   return dao.getAll();

}

public static List<Person> getAllActorsAndDirectors (int movieId){
    Movie movie = dao.getById(movieId);
   List<Person> persons = movie.getMoviePersons().stream().map(m -> m.getPerson()).toList();
   return persons;
}

public static List<Genre> getAllGenres (){
    return dao.getAllGenre();
}

public static List<Movie> getAllMoviesByGenre (int genreId){
    List<MovieGenre> movieGenres = dao.getAllMoviesByGenre(genreId);
    List<Movie> movies = movieGenres.stream().map(m -> m.getMovie()).toList();
    return movies;

}

public static Movie addMovie (Movie movie){
    return dao.createMovie(movie);

}

public static Movie updateMovie (Movie movie){
        return dao.update(movie);

}

public static Boolean deleteMovie (Movie movie){
        return dao.delete(movie.getMovieId());

}

public static List<Movie> searchByTitle (String title){
    List<Movie> movies = dao.getAll();
    return movies.stream().filter(m -> m.getTitle() != null && m.getTitle().toLowerCase().contains(title.toLowerCase())).toList();

    //Kunne også gøres via dao med LOWER(m.title) LIKE LOWER (:title) for ikke at hente det hele ud og først derefter filtrer
}

public static List<Movie> getTop10Highest (){
    List<Movie> movies = dao.getAll();
    List<Movie> sortedMovies = movies.stream().sorted(Comparator.comparingDouble(Movie::getAvgRating).reversed()).limit(10).toList();
    return sortedMovies;
}


public static List<Movie> getTop10Lowest (){
        List<Movie> movies = dao.getAll();
        List<Movie> sortedMovies = movies.stream().sorted(Comparator.comparingDouble(Movie::getAvgRating)).limit(10).toList();
        return sortedMovies;
}


public static List<Movie> getAllMoviesByRating (){
        List<Movie> movies = dao.getAll();
        List<Movie> sortedMovies = movies.stream().sorted(Comparator.comparingDouble(Movie::getAvgRating).reversed()).toList();
        return sortedMovies;
}



    // ------------------  Fetching  ----------------------------


    public static List<MovieDTO> loadAllMovies() throws IOException, InterruptedException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        int page = 1;
        int totalPages = 1;//den rigtige værdi bliver hentet inde i loopet
        MovieResponseDTO movieResponseDTO = null;
        List<MovieDTO> allDanishMovies = new ArrayList<>();

        while (page <= totalPages){

            String url = BASE_URL_DISCOVER+"?include_adult=false&include_video=false&primary_release_date.gte=2020-09-16&sort_by=popularity.desc&with_original_language=da&api_key="+API_KEY + "&page=" + page ;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            movieResponseDTO = mapper.readValue(response.body(),MovieResponseDTO.class);
            if (page == 1) {
                totalPages = movieResponseDTO.getTotal_pages();
                System.out.println(totalPages);
            }

            allDanishMovies.addAll(movieResponseDTO.getResults());
            page++;

        }

        return allDanishMovies;


    }

    public static List<GenreDTO> getAllGenre()throws IOException, InterruptedException{
        String url = String.format("https://api.themoviedb.org/3/genre/movie/list?language=da&api_key="+API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        GenreResponseDTO genreDTO = mapper.readValue(response.body(), GenreResponseDTO.class);
        List<GenreDTO> genreDTOS = new ArrayList<>();
        genreDTOS.addAll(genreDTO.genreDTOS());

        return genreDTOS;

    }

    public static List<PersonDTO> getAllPersonsByMovieId (MovieDTO movieDTO)throws IOException, InterruptedException{
        String url = String.format("https://api.themoviedb.org/3/movie/"+ movieDTO.getMovieId() + "/credits?language=da-DK&api_key="+API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        PersonResponseDTO responseDTO = mapper.readValue(response.body(), PersonResponseDTO.class);
        List<PersonDTO> personDTOS = new ArrayList<>();
        personDTOS.addAll(responseDTO.personDTOS());

        List<PersonDTO> actors = new ArrayList<>();

       for(PersonDTO p : personDTOS){
           if (p.getJob().equals(Jobs.Acting)){
               actors.add(p);
           }else if (p.getJob().equals(Jobs.Directing)) {
               movieDTO.setDirector(p);
           }
       }

        return personDTOS;
    }


    public static void loadAll() throws IOException, InterruptedException {
        Converters converter = new Converters();

        //----- Genre -----
        List<GenreDTO> genreDTOS = MovieService.getAllGenre();
        List<Genre> genres = genreDTOS.stream()
                .map(converter::convertGenreDtoToGenre)
                .toList();

        for(Genre g : genres){
            dao.createGenre(g);
        }

        // ----- Movies -----
        List<MovieDTO> movieDTOS = MovieService.loadAllMovies();

        for(MovieDTO movieDTO : movieDTOS){
            List<PersonDTO> personDTOS = getAllPersonsByMovieId(movieDTO);  //Persons bliver sat inde i convertMovieDtoToMovie()
            Movie movie = converter.convertMovieDtoToMovie(movieDTO,personDTOS,genreDTOS, emf);
            dao.update(movie);
        }


    }





}
