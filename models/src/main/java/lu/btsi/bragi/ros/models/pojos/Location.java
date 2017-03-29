/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.models.pojos;


import org.jooq.types.UInteger;

import javax.annotation.Generated;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Location implements Serializable {

    private static final long serialVersionUID = 828100727;

    private UInteger  id;
    private String    description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Location() {}

    public Location(Location value) {
        this.id = value.id;
        this.description = value.description;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
    }

    public Location(
        UInteger  id,
        String    description,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UInteger getId() {
        return this.id;
    }

    public void setId(UInteger id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "N\u00b0 " + id + ": " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!id.equals(location.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
