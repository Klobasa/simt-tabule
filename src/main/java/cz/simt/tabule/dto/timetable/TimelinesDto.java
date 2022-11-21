package cz.simt.tabule.dto.timetable;


import lombok.Data;

import java.util.List;

@Data
public class TimelinesDto {
    private String lineNumber;
    private List<RoutesDto> routes;
    private String startStation;
    private String endStation;

    public TimelinesDto(String lineNumber, List<RoutesDto> routes, String startStation, String endStation) {
        this.lineNumber = lineNumber;
        this.routes = routes;
        this.startStation = startStation;
        this.endStation = endStation;
    }
}
