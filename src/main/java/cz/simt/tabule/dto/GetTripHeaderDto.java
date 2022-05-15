package cz.simt.tabule.dto;

import cz.simt.tabule.data.Line;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetTripHeaderDto {
    private String id;
    private Line line;
    private GetTripDto startStation;
    private GetTripDto endStation;
    private GetTripDto actualStation;
    private LocalDateTime departureFromActualStation;

    public GetTripHeaderDto() {}

    public GetTripHeaderDto(String id, Line line, GetTripDto startStation, GetTripDto endStation, GetTripDto actualStation, LocalDateTime departureFromActualStation) {
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

    public GetTripDto getStartStation() {
        return startStation;
    }

    public void setStartStation(GetTripDto startStation) {
        this.startStation = startStation;
    }

    public GetTripDto getEndStation() {
        return endStation;
    }

    public void setEndStation(GetTripDto endStation) {
        this.endStation = endStation;
    }

    public GetTripDto getActualStation() {
        return actualStation;
    }

    public void setActualStation(GetTripDto actualStation) {
        this.actualStation = actualStation;
    }

    public LocalDateTime getDepartureFromActualStation() {
        return departureFromActualStation;
    }

    public void setDepartureFromActualStation(LocalDateTime departureFromActualStation) {
        this.departureFromActualStation = departureFromActualStation;
    }
}
