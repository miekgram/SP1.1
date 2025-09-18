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
public class Person {
    @Id
   private int id;
   private int gender;
   private String name;
   private String job;
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
   private List<MoviePerson> movies = new ArrayList<>();
}
