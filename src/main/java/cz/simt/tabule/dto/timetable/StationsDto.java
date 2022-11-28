package cz.simt.tabule.dto.timetable;

import lombok.Data;

import java.util.List;

@Data
public class StationsDto {
    private String name;
    private int sequence;
    private List<String> times_spicka_A;
    private List<String> times_sedlo_A;
    private List<String> times_noc_A;
    private List<String> times_spicka_B;
    private List<String> times_sedlo_B;
    private List<String> times_noc_B;

    public StationsDto(String name, int sequence, List<String> times_spicka_A, List<String> times_sedlo_A, List<String> times_noc_A, List<String> times_spicka_B, List<String> times_sedlo_B, List<String> times_noc_B) {
        this.name = name;
        this.sequence = sequence;
        this.times_spicka_A = times_spicka_A;
        this.times_sedlo_A = times_sedlo_A;
        this.times_noc_A = times_noc_A;
        this.times_spicka_B = times_spicka_B;
        this.times_sedlo_B = times_sedlo_B;
        this.times_noc_B = times_noc_B;
    }
}
