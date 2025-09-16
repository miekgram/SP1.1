package app.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private int movieId;
    private String title;
    private boolean adult;
    private List<GenreDTO> genreId;
    private String language;
    @JsonProperty("overview")
    private String description;
    private LocalDate releaseDate;
    @JsonProperty("vote_avg")
    private double avgRating;
}
