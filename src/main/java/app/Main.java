package app;


import app.Services.Converters;
import app.Services.MovieService;
import app.config.HibernateConfig;
import app.dao.Dao;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.PersonDTO;
import app.dtos.PersonResponseDTO;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("hej");

        MovieService.loadAll();
    }}





