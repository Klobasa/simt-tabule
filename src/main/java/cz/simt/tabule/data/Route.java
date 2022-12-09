package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Route {
    @Id
    @GeneratedValue
    private Long id;

    private String line;
    private String direction;
    private int sequence;
    private String station;
    private int time_spicka;
    private int time_sedlo;
    private int time_noc;
    private int traction;

    public Route() {

    }

    public Route(String line, String direction, int sequence, String station, int time_spicka, int time_sedlo, int time_noc, int traction) {
        this.line = line;
        this.direction = direction;
        this.sequence = sequence;
        this.station = station;
        this.time_spicka = time_spicka;
        this.time_sedlo = time_sedlo;
        this.time_noc = time_noc;
        this.traction = traction;
    }

    public String getLine() {
        return line;
    }

    public int getSequence() {
        return sequence;
    }

    public String getStation() {
        return station;
    }

    public String getStationWithTraction() {
        if (traction == 2) {
            return station + ":2";
        } else {
            return station + ":0";
        }
    }

    public int getTime_noc() {
        return time_noc;
    }

    public int getTime_sedlo() {
        return time_sedlo;
    }

    public int getTime_spicka() {
        return time_spicka;
    }

    public Long getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setDirection(final String direction) {
        this.direction = direction;
    }

    public void setLine(final String line) {
        this.line = line;
    }

    public void setStation(final String station) {
        this.station = station;
    }

    public void setTime_noc(final int time_noc) {
        this.time_noc = time_noc;
    }

    public void setTime_sedlo(final int time_sedlo) {
        this.time_sedlo = time_sedlo;
    }

    public void setTime_spicka(final int time_spicka) {
        this.time_spicka = time_spicka;
    }

    public void setSequence(final int order) {
        this.sequence = order;
    }

    public int getTraction() {
        return traction;
    }

    public void setTraction(int traction) {
        this.traction = traction;
    }
}
