package cz.simt.tabule.dto;

import lombok.Data;

@Data
public class GetFullTripDto {
    private String station;
    private int sequence;
    private int isAtStation;
    private String departure;

    public GetFullTripDto(String station, int sequence, int isAtStation, String departure) {
        this.station = station;
        this.sequence = sequence;
        this.isAtStation = isAtStation;
        this.departure = departure;
    }
}
