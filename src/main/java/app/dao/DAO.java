package app.dao;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DAO {

        private EntityManagerFactory emf;

        public void DAO(EntityManagerFactory emf) {
            this.emf = emf;
        }

    public DAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /** Gemmer n points (0..n-1) og returnerer hvor mange der blev gemt */
        public int savePoints(int n) {
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                for (int i = 0; i < n; i++) {
                    em.persist(new Movie(i, i));
                    // valgfrit batching:
                    if (i % 50 == 0) { em.flush(); em.clear(); }
                }
                tx.commit();
                return n;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            } finally {
                em.close();
            }
        }

        /** Returnerer antal Point-rækker */
        public long countPoints() {
            EntityManager em = emf.createEntityManager();
            try {
                Query q = em.createQuery("SELECT COUNT(p) FROM Movie p");
                return (long) q.getSingleResult();
            } finally {
                em.close();
            }
        }

        /** Returnerer gennemsnittet af x */
        public double avgX() {
            EntityManager em = emf.createEntityManager();
            try {
                Query q = em.createQuery("SELECT AVG(p.x) FROM Movie p");
                // getSingleResult() returnerer Double for AVG
                return ((Double) q.getSingleResult());
            } finally {
                em.close();
            }
        }

        /** Henter alle points */
        public List<Movie> findAll() {
            EntityManager em = emf.createEntityManager();
            try {
                TypedQuery<Movie> q = em.createQuery("SELECT p FROM Movie p ORDER BY p.id", Movie.class);
                return q.getResultList();
            } finally {
                em.close();
            }
        }
    }


