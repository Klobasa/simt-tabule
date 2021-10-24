package cz.simt.tabule.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class GetStationDto {
    private int id;
    private String line;
    private String endStation;
    private String station;
    private int isAtStation;
    private String departure;
    private int timeToDeparture;
    private int delay;

    public GetStationDto(int id, String line, String endStation, String station, String departure, int delay, int isAtStation, int timeToDeparture) {
        this.id = id;
        this.line = line;
        this.endStation = endStation;
        this.station = station;
        this.departure = departure;
        this.delay = delay;
        this.isAtStation = isAtStation;
        this.timeToDeparture = timeToDeparture;
    }
}
