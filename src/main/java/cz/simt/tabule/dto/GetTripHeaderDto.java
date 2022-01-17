package cz.simt.tabule.dto;

import cz.simt.tabule.data.Line;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetTripHeaderDto {
    private String id;
    private Line line;
    private String startStation;
    private String endStation;
    private String actualStation;
    private LocalDateTime departureFromActualStation;

    public GetTripHeaderDto() {}

    public GetTripHeaderDto(String id, Line line, String startStation, String endStation, String actualStation, LocalDateTime departureFromActualStation) {
        this.id = id;
        this.line = line;
        this.startStation = startStation;
        this.endStation = endStation;
        this.actualStation = actualStation;
        this.departureFromActualStation = departureFromActualStation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getActualStation() {
        return actualStation;
    }

    public void setActualStation(String actualStation) {
        this.actualStation = actualStation;
    }

    public LocalDateTime getDepartureFromActualStation() {
        return departureFromActualStation;
    }

    public void setDepartureFromActualStation(LocalDateTime departureFromActualStation) {
        this.departureFromActualStation = departureFromActualStation;
    }
}
