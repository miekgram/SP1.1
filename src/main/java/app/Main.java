package app;


import app.Services.Converters;
import app.Services.MovieService;
import app.config.HibernateConfig;
import app.dao.Dao;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.PersonDTO;
import app.dtos.PersonResponseDTO;
import app.entities.Genre;
import app.entities.Jobs;
import app.entities.Movie;
import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("hej");
        //MovieService.loadAll();


// ----- GetAllMovies -----
        //List<Movie> movies = MovieService.getAllMovies();
        //movies.forEach(movie -> System.out.println(movie));

// ----- GetAllActorsAndDirectors -----
        List<Person> actorsAndDirector = MovieService.getAllActorsAndDirectors(1232827);
        actorsAndDirector.forEach(person -> System.out.println(person));

// ----- GetAllGenre -----
        System.out.println(MovieService.getAllGenres());

// ----- GetAllMoviesByGenreId -----
        System.out.println(MovieService.getAllMoviesByGenre(35));

// ----- AddMovie -----
        Person mie = Person.builder()
                .id(1)
                .gender(1)
                .name("Mie")
                .job("Directing")
                .build();

        Movie movie = Movie.builder()
                .movieId(1234)
                .title("Malene og Mies eventyr")
                .adult(false)
                .description("To piger der elsker kaffe ")
                .language("Dansk")
                .director(mie)
                .avgRating(10)
                .genre(List.of(27))
                .build();

        movie.addPerson(mie);

        System.out.println(MovieService.addMovie(movie));

        movie.setDescription("To piger, der bager en rigtig god pumpkin pie");
        System.out.println(MovieService.updateMovie(movie));

// ----- deleteMovie -----
        System.out.println(MovieService.deleteMovie(movie));


// ----- SearchByTitle -----

        System.out.println(MovieService.searchByTitle("the girl with the needle"));

        System.out.println(MovieService.getTop10Highest());

        System.out.println(MovieService.getTop10Lowest());

        System.out.println(MovieService.getAllMoviesByRating());

    }}





