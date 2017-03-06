/*
 * This file is generated by jOOQ.
*/
package lu.btsi.bragi.ros.models.pojos;


import org.jooq.types.UInteger;

import javax.annotation.Generated;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class Product implements Serializable {

    private static final long serialVersionUID = -1645283095;

    private UInteger   id;
    private BigDecimal price;
    private UInteger   productCategoryId;
    private UInteger   locationId;
    private Timestamp  createdAt;
    private Timestamp  updatedAt;

    public Product() {}

    public Product(Product value) {
        this.id = value.id;
        this.price = value.price;
        this.productCategoryId = value.productCategoryId;
        this.locationId = value.locationId;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
    }

    public Product(
        UInteger   id,
        BigDecimal price,
        UInteger   productCategoryId,
        UInteger   locationId,
        Timestamp  createdAt,
        Timestamp  updatedAt
    ) {
        this.id = id;
        this.price = price;
        this.productCategoryId = productCategoryId;
        this.locationId = locationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UInteger getId() {
        return this.id;
    }

    public void setId(UInteger id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UInteger getProductCategoryId() {
        return this.productCategoryId;
    }

    public void setProductCategoryId(UInteger productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public UInteger getLocationId() {
        return this.locationId;
    }

    public void setLocationId(UInteger locationId) {
        this.locationId = locationId;
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
        return "N° "+id+", Price: "+price+"€";
    }
}
