package app;


import app.Services.Converters;
import app.Services.MovieService;
import app.config.HibernateConfig;
import app.dao.Dao;
import app.dtos.MovieDTO;
import app.entities.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.Converter;
import jakarta.persistence.EntityManagerFactory;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("hej");

       List<MovieDTO> movieDTOS = MovieService.getAllDanishMovies();
       Converters converter = new Converters();

        Dao dao = new Dao(emf);
        List<Movie> movies = movieDTOS.stream()
                .map(converter::convertMovieDtoToMovie)
                .toList();

        for(Movie movie : movies){
            dao.create(movie);



        }



    }
}



