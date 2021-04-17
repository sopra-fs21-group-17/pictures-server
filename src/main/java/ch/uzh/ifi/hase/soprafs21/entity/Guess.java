package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Dictionary;




/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "Guess")
public class Guess implements Serializable {


    @Column
    private String guess;
    @Id
    private Long idG;


    public String getGuess() { return guess; }

    public void setGuess(String guess) { this.guess = guess; }

    public void setIdG(Long idG) {
        this.idG = idG;
    }

    public Long getIdG() {
        return idG;
    }

}
