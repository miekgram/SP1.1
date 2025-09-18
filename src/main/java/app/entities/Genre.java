package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    private int id;
    private String name;

    //------@OneToMany = en Genre har mange rækker i koblingsentiteten MovieGenre
    //------mappedBy = "genre" = Fortæller at den anden side ejer relationen. Her peger "genre" på feltet i MovieGenre-klassen:
    //------cascade = CascadeTyp.ALL = Operationer på Genre “smitter af” på dens movieGenres eller forældrene smitter af på deres børn
    //------fetch = FetchType.EAGER = henter alt der er i tabellen / fetcher alt

    @OneToMany(mappedBy = "genre", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<MovieGenre> movieGenres = new ArrayList<>();
}
