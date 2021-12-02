package cz.simt.tabule.data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

@Entity
public class Player {

    @Id
    private String id;
    private String nick;
    private String line;
    private String route;
    private String vehicle;
    private LocalDateTime startTime;
    private LocalTime time;
    private String station;
    private LocalDateTime updated;
    private String endStation;
    private int delay;

    @Nullable
    private char checked;
    private LocalTime timeLoaded;

    public Player() {

    }

    public Player(String id, String line, String route, String vehicle, LocalDateTime startTime, LocalTime time, String station, String endStation, int delay, LocalDateTime updated) {
        this.id = id;
        this.line = line;
        this.route = route;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.time = time;
        this.station = station;
        this.endStation = endStation;
        this.delay = delay;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(final String route) {
        this.route = route;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public LocalTime getTimeLoaded() {
        return timeLoaded;
    }

    public void setTimeLoaded(final LocalTime timeLoaded) {
        this.timeLoaded = timeLoaded;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final LocalDateTime loaded) {
        this.updated = loaded;
    }

    public char getChecked() {
        return checked;
    }

    public void setChecked(final char checked) {
        this.checked = checked;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(final String vehicle) {
        this.vehicle = vehicle;
    }

    public String getLine() {
        return line;
    }

    public void setLine(final String line) {
        this.line = line;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(final String station) {
        this.station = station;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(final String endStation) {
        this.endStation = endStation;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Player [id=" + id + ", nick=" + nick + ", line=" + line + "route=" + route + ", startTime=" + startTime + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        Player that = (Player) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(line, that.line) &&
                Objects.equals(route, that.route) &&
                Objects.equals(vehicle, that.vehicle) &&
                Objects.equals(startTime, that.startTime);
    }
}
