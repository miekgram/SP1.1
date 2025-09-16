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
    @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<MovieGenre> movieGenres = new ArrayList<>();
}
