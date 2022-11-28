package cz.simt.tabule.dto.timetable;


import lombok.Data;

import java.util.List;

@Data
public class TimelinesDto {
    private String lineNumber;
    private List<StationsDto> stations;
    private String startStation;
    private String endStation;

    public TimelinesDto(String lineNumber, List<StationsDto> stations, String startStation, String endStation) {
        this.lineNumber = lineNumber;
        this.stations = stations;
        this.startStation = startStation;
        this.endStation = endStation;
    }
}
