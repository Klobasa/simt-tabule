package cz.simt.tabule.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GroupStation {
    @Id
    private int id;

    private String name;
    private String ids;
    private String urlName;

    public GroupStation() {

    }

    public GroupStation(int id, String name, String ids, String urlName) {
        this.id = id;
        this.name = name;
        this.ids = ids;
        this.urlName = urlName;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(final String ids) {
        this.ids = ids;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(final String urlName) {
        this.urlName = urlName;
    }
}
