package lu.btsi.bragi.ros.client.settings;

/**
 * Created by gillesbraun on 20/03/2017.
 */
public class InvoiceSettings {
    private String title = "Title";
    private String address = "Address 1";
    private String address2 = "Address 2";
    private String taxNumber = "1234TAX";
    private String telephone = "12345TEL";
    private String email = "email@email.co";

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress2() {
        return address2;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }
}
