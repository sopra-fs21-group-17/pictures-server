package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COUNTDOWN")
public class Countdown implements Serializable {

    @Id
    @GeneratedValue
    private Long countdownId;

    @Column
    private int count;


    public void setCountdownId(Long countdownId) {
        this.countdownId = countdownId;
    }

    @Id
    public Long getCountdownId() {
        return countdownId;
    }

    public void setCount(int count) { this.count = count; }

    public int getCount() { return count; }


}
