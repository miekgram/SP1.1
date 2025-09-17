package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder

public class Movie {

    @Id
    private int movieId;
    @Column(length = 500, nullable = false)
    private String title;
    private boolean adult;
    @ElementCollection //Gemmer liste af genre for filmen i db
    private List<Integer> genre;
    private String language;
    @Column(length = 5000, nullable = false)
    private String description;
    private double avgRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<MovieGenre> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<MoviePerson> moviePersons = new ArrayList<>();
    @ManyToOne
    private Person director;

    public MoviePerson addPerson(Person person){
        // defensiv beskyttelse hvis noget alligevel er null
        if (this.moviePersons == null) this.moviePersons = new ArrayList<>();
        if (person.getMovies() == null) person.setMovies(new ArrayList<>());

        //undgå dublet (samme person & job på samme film)
        //Vi kunne også have brugt hashSet til dette, for at undgå dubletter
        for (MoviePerson mp : moviePersons) {
            if (mp.getPerson().getId() == person.getId() && person.getJob().equals(mp.getJob())) return mp;
        }

        MoviePerson mp = new MoviePerson();
        mp.setMovie(this);
        mp.setPerson(person);
        mp.setJob(person.getJob());

        this.moviePersons.add(mp);
        person.getMovies().add(mp);
        return mp;
    }

    public MovieGenre addGenre(Genre genre){
        if (this.movieGenres == null) this.movieGenres = new ArrayList<>();
        if (genre.getMovieGenres() == null) genre.setMovieGenres(new ArrayList<>());

        // undgå dublet
        for (MovieGenre mg : movieGenres) {
            if (mg.getGenre().getId() == genre.getId()) return mg;
        }

        MovieGenre mg = new MovieGenre();
        mg.setMovie(this);
        mg.setGenre(genre);

        this.movieGenres.add(mg);
        genre.getMovieGenres().add(mg);

        return mg;

    }




}
