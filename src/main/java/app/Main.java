package app;


import app.Services.MovieService;
import app.config.HibernateConfig;
import app.dtos.MovieDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("hej");

        System.out.println(MovieService.getAllDanishMovies());

    }
}



