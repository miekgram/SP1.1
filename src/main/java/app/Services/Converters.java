package app.Services;

import app.dao.Dao;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.PersonDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;



public class Converters {

    public Movie convertMovieDtoToMovie (MovieDTO movieDTO, List<PersonDTO> persons, List<GenreDTO> genreDTOS, EntityManagerFactory emf){
        Dao dao = new Dao(emf);
        Movie movie = Movie.builder()
                .movieId(movieDTO.getMovieId())
                .title(movieDTO.getTitle())
                .adult(movieDTO.isAdult())
                .genre(movieDTO.getGenreIds())
                .description(movieDTO.getDescription())
                .language(movieDTO.getLanguage())
                .avgRating(movieDTO.getAvgRating())
                .build();

        for (PersonDTO p : persons) {
            Person person = convertPersonDtoToPerson(p);
            person = dao.updatePerson(person);          //Vi bruger update(merge) for ikke at konflikte med personid _pk
            movie.addPerson(person);                    //Person tilføjes som del af cast i en film
        }

        // Tilføj genres
        for (Integer genreId : movieDTO.getGenreIds()) {
            GenreDTO genreDTO = genreDTOS.stream()
                    .filter(g -> g.getId() == genreId)
                    .findFirst()
                    .orElse(null);

            if (genreDTO != null) {
                Genre genre = convertGenreDtoToGenre(genreDTO);
                movie.addGenre(genre);
            }
        }

        return movie;
    }

    public Genre convertGenreDtoToGenre (GenreDTO genreDTO){

        return Genre.builder()
                .id(genreDTO.getId())
                .name(genreDTO.getName())
                .build();
    }

    public Person convertPersonDtoToPerson (PersonDTO personDTO){

        return Person.builder()
                .job(personDTO.getJob())
                .name(personDTO.getName())
                .gender(personDTO.getGender())
                .id(personDTO.getId())
                .build();
    }
}
