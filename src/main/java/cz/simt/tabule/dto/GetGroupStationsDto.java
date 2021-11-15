package cz.simt.tabule.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetGroupStationsDto {
    private int id;
    private String name;
    private String urlName;
    private List<String> lines;


    public GetGroupStationsDto(int id, String name, String urlName, List<String> lines) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
        this.lines = lines;
    }
}
