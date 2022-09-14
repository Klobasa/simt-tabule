package cz.simt.tabule.dto.timetable;

import lombok.Data;

import java.util.List;

@Data
public class RoutesDto {
    private String direction;
    private List<StationsDto> stations;

    public RoutesDto(String direction, List<StationsDto> stations) {
        this.direction = direction;
        this.stations = stations;
    }
}
