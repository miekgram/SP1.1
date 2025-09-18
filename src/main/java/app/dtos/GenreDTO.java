package app.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)//ignorer andre felter fra JSON filen som ikke bliver til atributter i DTO
public class GenreDTO {
    private int id;
    private String name;


}
