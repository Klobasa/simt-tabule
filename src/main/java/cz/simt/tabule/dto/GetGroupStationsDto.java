package cz.simt.tabule.dto;

import lombok.Data;

@Data
public class GetGroupStationsDto {
    private int id;
    private String name;
    private String urlName;


    public GetGroupStationsDto(int id, String name, String urlName) {
        this.id = id;
        this.name = name;
        this.urlName = urlName;
    }
}
