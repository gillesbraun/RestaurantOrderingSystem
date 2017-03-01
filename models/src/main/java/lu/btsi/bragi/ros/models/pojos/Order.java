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
public class Order implements Serializable {

    private static final long serialVersionUID = -1588851679;

    private UInteger  id;
    private Byte      delivered;
    private UInteger  tableId;
    private UInteger  waiterId;
    private UInteger  invoiceId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Order() {}

    public Order(Order value) {
        this.id = value.id;
        this.delivered = value.delivered;
        this.tableId = value.tableId;
        this.waiterId = value.waiterId;
        this.invoiceId = value.invoiceId;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
    }

    public Order(
        UInteger  id,
        Byte      delivered,
        UInteger  tableId,
        UInteger  waiterId,
        UInteger  invoiceId,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {
        this.id = id;
        this.delivered = delivered;
        this.tableId = tableId;
        this.waiterId = waiterId;
        this.invoiceId = invoiceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UInteger getId() {
        return this.id;
    }

    public void setId(UInteger id) {
        this.id = id;
    }

    public Byte getDelivered() {
        return this.delivered;
    }

    public void setDelivered(Byte delivered) {
        this.delivered = delivered;
    }

    public UInteger getTableId() {
        return this.tableId;
    }

    public void setTableId(UInteger tableId) {
        this.tableId = tableId;
    }

    public UInteger getWaiterId() {
        return this.waiterId;
    }

    public void setWaiterId(UInteger waiterId) {
        this.waiterId = waiterId;
    }

    public UInteger getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(UInteger invoiceId) {
        this.invoiceId = invoiceId;
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
        StringBuilder sb = new StringBuilder("Order (");

        sb.append(id);
        sb.append(", ").append(delivered);
        sb.append(", ").append(tableId);
        sb.append(", ").append(waiterId);
        sb.append(", ").append(invoiceId);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(updatedAt);

        sb.append(")");
        return sb.toString();
    }
}
