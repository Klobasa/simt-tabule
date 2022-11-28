package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Timetable {
    @Id
    @GeneratedValue
    private Long id;

    private String line;
    private int sequence;
    private int group_station;
    private String time_spicka_A;
    private String time_sedlo_A;
    private String time_noc_A;
    private String time_spicka_B;
    private String time_sedlo_B;
    private String time_noc_B;

    public Timetable() {

    }

    public Timetable(String line, int group_station, String time_spicka_A, String time_sedlo_A, String time_noc_A, String time_spicka_B, String time_sedlo_B, String time_noc_B) {
        this.line = line;
        this.group_station = group_station;
        this.time_spicka_A = time_spicka_A;
        this.time_sedlo_A = time_sedlo_A;
        this.time_noc_A = time_noc_A;
        this.time_spicka_B = time_spicka_B;
        this.time_sedlo_B = time_sedlo_B;
        this.time_noc_B = time_noc_B;
    }

    public Timetable(String line, String direction, int group_station) {
        this.line = line;
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

    public String getTime_spicka_A() {
        return time_spicka_A;
    }

    public void setTime_spicka_A(String time_spicka_A) {
        this.time_spicka_A = time_spicka_A;
    }

    public String getTime_sedlo_A() {
        return time_sedlo_A;
    }

    public void setTime_sedlo_A(String time_sedlo_A) {
        this.time_sedlo_A = time_sedlo_A;
    }

    public String getTime_noc_A() {
        return time_noc_A;
    }

    public void setTime_noc_A(String time_noc_A) {
        this.time_noc_A = time_noc_A;
    }

    public String getTime_spicka_B() {
        return time_spicka_B;
    }

    public void setTime_spicka_B(String time_spicka_B) {
        this.time_spicka_B = time_spicka_B;
    }

    public String getTime_sedlo_B() {
        return time_sedlo_B;
    }

    public void setTime_sedlo_B(String time_sedlo_B) {
        this.time_sedlo_B = time_sedlo_B;
    }

    public String getTime_noc_B() {
        return time_noc_B;
    }

    public void setTime_noc_B(String time_noc_B) {
        this.time_noc_B = time_noc_B;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timetable timetable = (Timetable) o;
        return group_station == timetable.group_station && line.equals(timetable.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, group_station);
    }
}
