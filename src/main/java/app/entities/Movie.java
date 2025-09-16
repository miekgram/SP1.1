package app.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<MovieGenre> movieGenres = new ArrayList<>();
    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<MoviePerson> moviePersons = new ArrayList<>();



}
