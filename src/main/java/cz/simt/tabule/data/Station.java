package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.istack.NotNull;

@Entity
public class Station {

    @Id
    @GeneratedValue
    private Long id;

    private Long gameId;

    private int stationTraction;

    @NotNull
    private String stationName;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(final Long id) {
        this.gameId = id;
    }

    public int getStationTraction() {
        return stationTraction;
    }

    public void setStationTraction(final int stationTraction) {
        this.stationTraction = stationTraction;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(final String stationName) {
        this.stationName = stationName;
    }

}
