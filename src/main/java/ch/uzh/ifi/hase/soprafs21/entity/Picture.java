package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Entity
@Table(name = "PICTURE")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String pictureLink;


    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}
