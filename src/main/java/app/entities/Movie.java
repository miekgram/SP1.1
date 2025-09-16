package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {

    @Id
    int movieId;
    String title;
    boolean adult;
    @ElementCollection //Gemmer liste af genre for filmen i db
    List<Integer> genre;
    String language;
    String description;


}
