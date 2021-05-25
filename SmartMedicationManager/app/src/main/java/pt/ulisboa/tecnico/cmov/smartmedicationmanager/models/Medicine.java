package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Medicine {

    private String name;

    private int quantity;

    private String notes;

    @SerializedName("validity")
    private String expirationDate;

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
        DateFormat format = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setExpirationDate(Date expirationDate) {
        DateFormat format = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH);
        this.expirationDate = format.format(expirationDate);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
