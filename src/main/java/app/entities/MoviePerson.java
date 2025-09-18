package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity

public class MoviePerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//----autogenererer et id ved oprettelse
    private Integer id;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Person person;
    private String job;

}
