package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import java.time.LocalDateTime;

public class Prescription {

    private Medicine medicine;

    private int quantity;

    private String periodicity;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public Prescription(Medicine medicine, int quantity, String periodicity, LocalDateTime startDate) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.periodicity = periodicity;
        this.startDate = startDate;
    }

    public Prescription() {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getNextDate() {
        return "";
    }
}
