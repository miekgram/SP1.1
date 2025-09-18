package app.dao;

import app.entities.Genre;
import app.entities.Movie;
import app.entities.MovieGenre;
import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class Dao implements IDAO<Movie, Integer>{
    EntityManagerFactory emf;

    public Dao(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Movie createMovie(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(movie);
            em.getTransaction().commit();
        }
        return movie;
    }

    @Override
    public List<Movie> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery("select m from Movie m", Movie.class);
            return query.getResultList();
        }
    }

    @Override
    public Movie getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            //em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            //em.getTransaction().commit();
            return movie;
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie updatedMovie = em.merge(movie);
            em.getTransaction().commit();
            return updatedMovie;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person personToBeDeleted = em.find(Person.class, id);
            if (personToBeDeleted != null) {
                em.remove(personToBeDeleted);
                em.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        }
    }





    //-------------andre dao metoder end Movie-------------//


    public Genre createGenre(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        }
        return genre;
    }

    public Person createPerson(Person person) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        }
        return person;
    }

    public Person updatePerson(Person person) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person updatedPerson = em.merge(person);
            em.getTransaction().commit();
            return updatedPerson;
        }
    }


    public Genre getGenreById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            //em.getTransaction().begin();
            Genre genre = em.find(Genre.class, id);
            //em.getTransaction().commit();
            return genre;
        }
    }


    public List<Genre> getAllGenre() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery("select m from Genre m", Genre.class);
            return query.getResultList();
        }
    }


    public List<MovieGenre> getAllMoviesByGenre (int genreId){
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieGenre> query = em.createQuery("select m from MovieGenre m where genre.id = "+genreId, MovieGenre.class);
            return query.getResultList();
        }
    }



}
