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

//--- denne klasse er en form for oversættelses klasse mellem vores DTO klasser og entitiet klasser
//den konvatere/oversætter data fra API’et (DTO’er) til vores database-objekter (JPA-entiteter), så det kan bliver klart til at blive gemt i DB

public class Converters {

    //---vi laver et Movie objekt og propper vores movieDTO ind i så vi på den måde konvaterer dto til objekt
    //----Klassen bygger en komplet Movie-entitet ud fra ét MovieDTO og sætter de rigtige relationer på:

    public Movie convertMovieDtoToMovie (MovieDTO movieDTO, List<PersonDTO> persons, List<GenreDTO> genreDTOS, EntityManagerFactory emf){
        Dao dao = new Dao(emf);//DAO’en skal bruges til at skrive/opdatere Person i databasen.
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
            movie.addPerson(person);                    //Person tilføjes som del af cast i en film //movie.addPerson(...) (laver MoviePerson-koblinger).
        }

        // Tilføj genres
        for (Integer genreId : movieDTO.getGenreIds()) {
            GenreDTO genreDTO = genreDTOS.stream()
                    .filter(g -> g.getId() == genreId)
                    .findFirst()
                    .orElse(null);

            if (genreDTO != null) {
                Genre genre = convertGenreDtoToGenre(genreDTO);
                movie.addGenre(genre);//movie.addGenre(...) ( laver MovieGenre-koblinger).
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
