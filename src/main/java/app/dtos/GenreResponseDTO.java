package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

//-----record er en kompakt, immutabel klasse til at bære data, rigtig god til en kortfattet DTO

public record GenreResponseDTO(@JsonProperty("genres") List<GenreDTO> genreDTOS){

}
