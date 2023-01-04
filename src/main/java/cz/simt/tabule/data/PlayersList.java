package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PlayersList {
    @Id
    @GeneratedValue
    private Long id;

    private String nick;

    public PlayersList() {}

    public PlayersList(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
