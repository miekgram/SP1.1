package app.dao;

import app.entities.Movie;
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
    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
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
}
