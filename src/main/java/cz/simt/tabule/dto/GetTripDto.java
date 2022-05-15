package cz.simt.tabule.dto;

import lombok.Data;

@Data
public class GetTripDto {
    private String station;
    private int sequence;
    private int isAtStation;
    private String departure;

    public GetTripDto(String station, int sequence, int isAtStation, String departure) {
        this.station = station;
        this.sequence = sequence;
        this.isAtStation = isAtStation;
        this.departure = departure;
    }

    public GetTripDto(String station, String departure) {
        this.station = station;
        this.departure = departure;
    }

    public GetTripDto() {

    }
}
