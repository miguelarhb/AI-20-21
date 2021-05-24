package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import java.time.LocalDateTime;

public class Alarm {

    private LocalDateTime dateTime;

    private boolean taken;

    public Alarm(LocalDateTime dateTime, boolean taken) {
        this.dateTime = dateTime;
        this.taken = taken;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
