package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record GenreResponseDTO(@JsonProperty("genres") List<GenreDTO> genreDTOS){

}
