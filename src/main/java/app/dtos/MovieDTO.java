package app.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

//-------- en DTO (Data Transfer Object) bruges til at bære data mellem TMDb API’et og vores service-lag.
//-------- den indeholder INGEN forretningslogik og er ikke knyttet til databasen - (ingen JPA-annoteringer)
//-------- feltnavne og @JsonProperty sørger for at JSON fra API’et mappes korrekt til Javafelter.


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    @JsonProperty("id")//------- sætter en anden værdi så den matcher JSON
    private int movieId;//-------- her kalder vi den så hvad vi vil og bruger den i java
    private String title;
    private boolean adult;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("original_language")
    private String language;
    @JsonProperty("overview")
    private String description;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("vote_average")
    private double avgRating;
    private PersonDTO director;

}
