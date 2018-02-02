package app.models;

import javax.persistence.*;

@Entity
@Table(name="matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}
