package app.Services;


import app.config.HibernateConfig;
import app.dao.Dao;
import app.dtos.*;
import app.entities.Genre;
import app.entities.Jobs;
import app.entities.Movie;
import app.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MovieService  {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static Dao dao = new Dao(emf);



    private static final String API_KEY = System.getenv("api_key");  // Replace with your TMDb API Key
    private static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_DISCOVER = "https://api.themoviedb.org/3/discover/movie";
    ;


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


    public static void loadAllActorsAndGenre(List<MovieDTO> movieDTOS)throws IOException, InterruptedException{
        List<PersonDTO> personDTOS;
        Converters converters = new Converters();
        //For hver film tilføjer vi actors og genre til movie listerne med persons og genres
        for (MovieDTO m : movieDTOS){
            Movie movie = dao.getById(m.getMovieId());
            personDTOS = getAllPersonsByMovieId(m);

            for (Integer genreId : m.getGenreIds()) {
                Genre genre = dao.getGenreById(genreId);
                movie.addGenre(genre);
            }

            for (PersonDTO p : personDTOS){
               Person person = converters.convertPersonDtoToPerson(p);
               dao.updatePerson(person);
                movie.addPerson(person);
            }
            dao.update(movie);
        }


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
            List<PersonDTO> personDTOS = getAllPersonsByMovieId(movieDTO);
            Movie movie = converter.convertMovieDtoToMovie(movieDTO,personDTOS,genreDTOS, emf);
            dao.update(movie);
        }


    }





}
