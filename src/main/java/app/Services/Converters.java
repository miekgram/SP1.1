package app.Services;

import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.PersonDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;

public class Converters {

    public Movie convertMovieDtoToMovie (MovieDTO movieDTO){

        return Movie.builder()
                .movieId(movieDTO.getMovieId())
                .title(movieDTO.getTitle())
                .adult(movieDTO.isAdult())
                .genre(movieDTO.getGenreId())
                .description(movieDTO.getDescription())
                .language(movieDTO.getLanguage())
                .avgRating(movieDTO.getAvgRating())
                .build();

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
