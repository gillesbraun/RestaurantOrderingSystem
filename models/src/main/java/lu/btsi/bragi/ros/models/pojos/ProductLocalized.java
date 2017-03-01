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
public class ProductLocalized implements Serializable {

    private static final long serialVersionUID = -1872168795;

    private UInteger  productId;
    private String    languageCode;
    private String    label;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ProductLocalized() {}

    public ProductLocalized(ProductLocalized value) {
        this.productId = value.productId;
        this.languageCode = value.languageCode;
        this.label = value.label;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
    }

    public ProductLocalized(
        UInteger  productId,
        String    languageCode,
        String    label,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {
        this.productId = productId;
        this.languageCode = languageCode;
        this.label = label;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UInteger getProductId() {
        return this.productId;
    }

    public void setProductId(UInteger productId) {
        this.productId = productId;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        StringBuilder sb = new StringBuilder("ProductLocalized (");

        sb.append(productId);
        sb.append(", ").append(languageCode);
        sb.append(", ").append(label);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(updatedAt);

        sb.append(")");
        return sb.toString();
    }
}
