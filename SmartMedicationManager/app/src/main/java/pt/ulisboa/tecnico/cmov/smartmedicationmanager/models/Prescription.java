package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import java.util.Date;

public class Prescription {

    private Medicine medicine;

    private int quantity;

    private String periodicity;

    private Date startDate;

    private Date endDate;

    public Prescription(Medicine medicine, int quantity, String periodicity, Date startDate) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.periodicity = periodicity;
        this.startDate = startDate;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
