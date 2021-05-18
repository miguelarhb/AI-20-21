package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import java.util.Date;

public class Medicine {

    private String name;

    private int quantity;

    private String notes;

    private Date expirationDate;

    private String barcode;

    public Medicine(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Medicine() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
