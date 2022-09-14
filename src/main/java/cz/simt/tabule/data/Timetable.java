package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Timetable {
    @Id
    @GeneratedValue
    private Long id;

    private String line;
    private String direction;
    private int sequence;
    private int group_station;
    private String time_spicka;
    private String time_sedlo;
    private String time_noc;

    public Timetable() {

    }

    public Timetable(String line, String direction, int group_station, String time_spicka, String time_sedlo, String time_noc) {
        this.line = line;
        this.direction = direction;
        this.group_station = group_station;
        this.time_spicka = time_spicka;
        this.time_sedlo = time_sedlo;
        this.time_noc = time_noc;
    }

    public Timetable(String line, String direction, int group_station) {
        this.line = line;
        this.direction = direction;
        this.group_station = group_station;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getGroup_station() {
        return group_station;
    }

    public void setGroup_station(int group_station) {
        this.group_station = group_station;
    }

    public String getTime_spicka() {
        return time_spicka;
    }

    public void setTime_spicka(String time_spicka) {
        this.time_spicka = time_spicka;
    }

    public String getTime_sedlo() {
        return time_sedlo;
    }

    public void setTime_sedlo(String time_sedlo) {
        this.time_sedlo = time_sedlo;
    }

    public String getTime_noc() {
        return time_noc;
    }

    public void setTime_noc(String time_noc) {
        this.time_noc = time_noc;
    }
}
