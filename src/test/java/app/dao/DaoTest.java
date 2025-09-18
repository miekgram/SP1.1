package app.dao;

import app.Main;
import app.config.HibernateConfig;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DaoTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final Dao dao = new Dao(emf);
    private Movie movie = new Movie();
    private Person mie;


    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.getTransaction().begin();

            // Slet afhængige entiteter først for at undgå PK-fejl
            em.createQuery("DELETE FROM MoviePerson").executeUpdate();
            em.createQuery("DELETE FROM MovieGenre").executeUpdate();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.getTransaction().commit();

            em.getTransaction().begin();


            Genre genre = Genre.builder()
                    .id(27)
                    .name("Gyser")
                    .build();
            em.merge(genre);

            mie = Person.builder()
                    .id(1)
                    .gender(1)
                    .name("Mie")
                    .job("Directing")
                    .build();
            em.merge(mie);

            //Kan laves som populator og hente entiteter med array.
            movie = Movie.builder()
                    .movieId(1234)
                    .title("Malene og Mies eventyr")
                    .adult(false)
                    .description("To piger der elsker kaffe ")
                    .language("Dansk")
                    .director(mie)
                    .avgRating(10)
                    .genre(List.of(27))
                    .build();

            movie.addPerson(mie);
            movie.addGenre(genre);

            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
       if(emf != null) emf.close();
    }

    @Test
    void createMovie() {
        Movie actual = dao.createMovie(Movie.builder()
                .movieId(1234)
                .title("Malene og Mies eventyr")
                .adult(false)
                .description("To piger der elsker kaffe ")
                .language("Dansk")
                .director(mie)
                .avgRating(10)
                .genre(List.of(27))
                .build());
        assertEquals(movie,actual);


//        Movie persisted = dao.createMovie(movie); // merge(movie)
//        assertNotNull(persisted);
//        Movie found = dao.getById(movie.getMovieId());
//        assertNotNull(found);
//        assertEquals("Malene og Mies eventyr", found.getTitle());
//        assertEquals("Dansk", found.getLanguage());
//        assertEquals(10.0, found.getAvgRating(), 0.0001);
//        assertNotNull(found.getDirector());
//        assertEquals("Mie", found.getDirector().getName());
    }

    @Test
    void getAll() {
        dao.createMovie(movie);
        Movie m2 = Movie.builder()
                .movieId(4321)
                .title("kaffe og kaos")
                .language("Dansk")
                .director(mie)
                .genre(List.of(27))
                .avgRating(8.0)
                .build();
        m2.addPerson(mie);
        dao.createMovie(m2);

        List<Movie> all = dao.getAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(m -> m.getMovieId() == movie.getMovieId()));
        assertTrue(all.stream().anyMatch(m -> m.getMovieId() == m2.getMovieId()));

    }

    @Test
    void getById() {

        Movie persisted = dao.createMovie(movie);
        Movie found = dao.getById(persisted.getMovieId());
        assertNotNull(found);
        assertEquals(persisted.getMovieId(), found.getMovieId());


//        dao.createMovie(movie);
//        Movie found = dao.getById(1234);
//        assertNotNull(found);
//        assertEquals(1234, found.getMovieId());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void createGenre() {
    }

    @Test
    void createPerson() {
    }

    @Test
    void updatePerson() {
    }

    @Test
    void getGenreById() {
    }

    @Test
    void getAllGenre() {
    }

    @Test
    void getAllMoviesByGenre() {
    }
}