package app.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Person {
    @Id
   private int id;
   private int gender;
   private String name;
   private String job;
}
