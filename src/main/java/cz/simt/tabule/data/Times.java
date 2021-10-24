package cz.simt.tabule.data;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Times {
    @Id
    private String name;
    @NotNull
    private LocalDateTime time;

    public Times() {
    }

    public Times(String name, LocalDateTime time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
