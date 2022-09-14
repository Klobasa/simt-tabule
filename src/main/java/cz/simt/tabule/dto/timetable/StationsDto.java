package cz.simt.tabule.dto.timetable;

import lombok.Data;

import java.util.List;

@Data
public class StationsDto {
    private String name;
    private int sequence;
    private List<String> times_spicka;
    private List<String> times_sedlo;
    private List<String> times_noc;

    public StationsDto(String name, int sequence, List<String> times_spicka, List<String> times_sedlo, List<String> times_noc) {
        this.name = name;
        this.sequence = sequence;
        this.times_spicka = times_spicka;
        this.times_sedlo = times_sedlo;
        this.times_noc = times_noc;
    }
}
