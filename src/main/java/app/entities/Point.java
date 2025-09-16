package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int x;
    private int y;

public Point(int x, int y){
    this.x = x;
    this.y = y;

}
}
