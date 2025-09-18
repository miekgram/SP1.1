package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


//-----record er en kompakt, immutabel klasse til at bære data, rigtig god til en kortfattet DTO

@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonResponseDTO(@JsonProperty("cast") List<PersonDTO> personDTOS) {
}
