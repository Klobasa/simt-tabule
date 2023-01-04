package cz.simt.tabule.dto;

import lombok.Data;

@Data
public class GetPlayerIdDto {
    private int id;
    private String playerId;
    private String playerNick;

    public GetPlayerIdDto(int id, String playerId, String playerNick) {
        this.id = id;
        this.playerId = playerId;
        this.playerNick = playerNick;
    }
}
