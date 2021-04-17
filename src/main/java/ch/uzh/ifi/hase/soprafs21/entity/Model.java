package ch.uzh.ifi.hase.soprafs21.entity;


import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "Model")
public class Model implements Serializable {


    @Column
    private String model;
    @Id
    private Long idM;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setIdM(Long idM) {
        this.idM = idM;
    }

    public Long getIdM() {
        return idM;
    }
}

