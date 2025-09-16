package app.Services;


import app.dtos.MovieDTO;
import app.dtos.MovieResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private static final String API_KEY = System.getenv("api_key");  // Replace with your TMDb API Key
    private static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_DISCOVER = "https://api.themoviedb.org/3/discover/movie";


    public static List<MovieDTO> getAllDanishMovies() throws IOException, InterruptedException{
        ObjectMapper mapper = new ObjectMapper();

        String url = BASE_URL_DISCOVER+"?include_adult=false&include_video=false&page=1&primary_release_date.gte=2020-09-16&sort_by=popularity.desc&with_original_language=da&api_key="+API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        mapper.registerModule(new JavaTimeModule());

        MovieResponseDTO movieResponseDTO = mapper.readValue(response.body(),MovieResponseDTO.class);

        List<MovieDTO> allDanishMovies = new ArrayList<>();
        allDanishMovies.addAll(movieResponseDTO.getResults());

        return allDanishMovies;
    }


    public static String getMovieById(int id) throws IOException, InterruptedException {

        String url = BASE_URL_MOVIE + id + "?api_key=" + API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getMoviesByAvgVotingBetween(double min, double max, int page) throws IOException, InterruptedException {
        String url = String.format("%s?api_key=%s&page=%d&sort_by=popularity.desc&vote_average.gte=%.2f&vote_average.lte=%.2f", BASE_URL_DISCOVER, API_KEY, page, min, max);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
