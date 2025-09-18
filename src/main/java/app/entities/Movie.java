package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//I test crasher det pga af builder og equalsAndHashcode arbejder lige imod hinanden
//Derfor sætte vi denne på for at ekskluderer de felter der ikke er sat med builderen

public class Movie {

    @Id
    private int movieId;
    @Column(length = 500, nullable = false)
    private String title;
    private boolean adult;

    @ElementCollection (fetch = FetchType.EAGER)//Gemmer liste af genre for filmen i db
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Integer> genre;
    private String language;
    @Column(length = 5000, nullable = false)
    private String description;
    private double avgRating;
        //---- kobling mellem Movie og Genre entitet
        //------ mappedBy = "movie" = siden med MovieGenre ejer relationen i movie atributten
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default //---- listen starter som tom ved hjælp af builder()
    private List<MovieGenre> movieGenres = new ArrayList<>();
        //------ kobling melle Movie og MoviePerson
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<MoviePerson> moviePersons = new ArrayList<>();
    @ManyToOne
    @ToString.Exclude
    private Person director;

//Fælles pointe for begge metoder
//fungerer som hjælper/factory‐metoder til at bygge de bidirektionelle relationer korrekt
//de gør så samme kald flere gange giver ikke ekstra, identiske koblinger


//Formål: oprette/vedligeholde koblingen mellem en Movie og en Person via join‐entiteten MoviePerson

    public MoviePerson addPerson(Person person){
        // defensiv beskyttelse hvis noget alligevel er null
        if (this.moviePersons == null) this.moviePersons = new ArrayList<>();
        if (person.getMovies() == null) person.setMovies(new ArrayList<>());

        //undgå dublet (samme person & job på samme film)
        //Vi kunne også have brugt hashSet til dette, for at undgå dubletter
        for (MoviePerson mp : moviePersons) {
            if (mp.getPerson().getId() == person.getId() && person.getJob().equals(mp.getJob())) return mp;//Hvis der findes en kobling med samme person og job, returneres den eksisterende (ingen tilføjelse)
        }

        MoviePerson mp = new MoviePerson(); //----Opretter en ny koblingsentitet.
        //----Sætter relationerne (denne film, den givne person) og jobbet fra personen
        mp.setMovie(this);
        mp.setPerson(person);
        mp.setJob(person.getJob());
        //----Lægger koblingen på filmens og persones liste
        this.moviePersons.add(mp);
        person.getMovies().add(mp);
        return mp;//----retunere den oprettede kobling
    }


    //Formål: oprette/vedligeholde koblingen mellem en Movie og en Genre via join‐entiteten MovieGenre.

    public MovieGenre addGenre(Genre genre){
        if (this.movieGenres == null) this.movieGenres = new ArrayList<>(); //---Sikrer at filmens genre‐koblingsliste er initialiseret.
        if (genre.getMovieGenres() == null) genre.setMovieGenres(new ArrayList<>());//----Sikrer at genrens modsatte liste er initialiseret

        // undgå dublet
        for (MovieGenre mg : movieGenres) {
            if (mg.getGenre().getId() == genre.getId()) return mg; //Tjekker om genren allerede er tilføjet til filmen; hvis ja, returneres den eksisterende kobling.
        }

        MovieGenre mg = new MovieGenre();
        mg.setMovie(this);
        mg.setGenre(genre);

        this.movieGenres.add(mg);
        genre.getMovieGenres().add(mg);

        return mg;

    }




}
