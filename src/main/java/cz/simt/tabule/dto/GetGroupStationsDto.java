package cz.simt.tabule.dto;

import cz.simt.tabule.data.Line;
import lombok.Data;

import java.util.List;

@Data
public class GetGroupStationsDto {
    private int id;
    private String name;
    private String urlName;
    private List<Line> lines;


    public GetGroupStationsDto(int id, String name, String urlName, List<Line> lines) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
        this.lines = lines;
    }
}
