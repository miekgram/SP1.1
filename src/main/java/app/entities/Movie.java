package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Movie {

    @Id
    private int movieId;
    private String title;
    private boolean adult;
    @ElementCollection //Gemmer liste af genre for filmen i db
    private List<Integer> genre;
    private String language;
    private String description;
    private double avgRating;


}
