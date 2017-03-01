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
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = -296938219;

    private UInteger  id;
    private String    imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ProductCategory() {}

    public ProductCategory(ProductCategory value) {
        this.id = value.id;
        this.imageUrl = value.imageUrl;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
    }

    public ProductCategory(
        UInteger  id,
        String    imageUrl,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UInteger getId() {
        return this.id;
    }

    public void setId(UInteger id) {
        this.id = id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        StringBuilder sb = new StringBuilder("ProductCategory (");

        sb.append(id);
        sb.append(", ").append(imageUrl);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(updatedAt);

        sb.append(")");
        return sb.toString();
    }
}
