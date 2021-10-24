package cz.simt.tabule.data;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    private int sequence;
    private String playerId;
    private String stopId;
    private LocalTime time;
    private boolean position = false;

    public Trip() {}

    public Trip(int sequence, String playerId, String stopId, LocalTime time) {
        this.sequence = sequence;
        this.playerId = playerId;
        this.stopId = stopId;
        this.time = time;
    }

    public int getSequence() {
        return sequence;
    }

    public String getStopId() {
        return stopId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    public void setStopId(final String stopId) {
        this.stopId = stopId;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public void setPosition(final boolean position) {
        this.position = position;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public boolean getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Trip [sequence=" + sequence + ", stopId=" + stopId + ", time=" + time + "]\\r\\n";
    }
}
