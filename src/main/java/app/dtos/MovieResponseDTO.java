package app.dtos;

import lombok.Data;

import java.util.List;

@Data
public class MovieResponseDTO {
    private int page;
    private List<MovieDTO> results;
    private int total_pages;
    private int total_results;

}