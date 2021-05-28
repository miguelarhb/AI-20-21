package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alarm {

    @SerializedName("time")
    private String dateTime;

    private boolean taken;

    public Alarm() {
    }

    public LocalDateTime getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        this.dateTime = dateTime.format(formatter);
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
