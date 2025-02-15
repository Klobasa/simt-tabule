package cz.simt.tabule.data;

import java.time.LocalDateTime;

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
    private String stopName;
    private LocalDateTime time;
    private boolean position = false;

    public Trip() {}

    public Trip(int sequence, String playerId, String stopId, LocalDateTime time) {
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

    public String getStopName() {
        return stopName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    public void setStopId(final String stopId) {
        this.stopId = stopId;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void setTime(final LocalDateTime time) {
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
