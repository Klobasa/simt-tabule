package cz.simt.tabule.dto;

import lombok.Data;

@Data
public class GetPlayerIdDto {
    private int id;
    private String playerId;

    public GetPlayerIdDto(int id, String playerId) {
        this.id = id;
        this.playerId = playerId;
    }
}
