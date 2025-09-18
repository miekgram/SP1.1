package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MovieGenre> movieGenres = new ArrayList<>();
}
