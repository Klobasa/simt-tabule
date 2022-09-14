package cz.simt.tabule.dto.timetable;


import lombok.Data;

import java.util.List;

@Data
public class TimelinesDto {
    private String lineNumber;
    private List<RoutesDto> routes;

    public TimelinesDto(String lineNumber, List<RoutesDto> routes) {
        this.lineNumber = lineNumber;
        this.routes = routes;
    }
}
