package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.AlarmReceiver;

public class Prescription {

    private String id;

    private Medicine medicine;

    private int quantity;

    private String periodicity;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String notes;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void generateId(){
        this.id = UUID.randomUUID().toString();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAlarm(Context context, int alarm_id) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("id", this.id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id, myIntent, 0);

        long alarmStart = this.startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // if alarm time has already passed, increment by interval
        while (alarmStart <= System.currentTimeMillis()) {
            alarmStart += getInterval();
        }

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmStart, pendingIntent);
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        //TODO
        //ALARM_CODE = p.getId();
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 300, intent, 0);
        alarmManager.cancel(alarmPendingIntent);

    }

    public long getInterval(){
        long interval;
        if (this.periodicity.equals("test")){
            interval = 5 * 1000;
        }
        else{
            String[] fields = this.periodicity.split("-");
            int nr = Integer.parseInt(fields[0]);
            String type = fields[1];
            if (type.equals("Hours")){
                interval = 1000 * 60 * 60 * nr;
            }
            else{
                interval = 1000 * 60 * 60 * 24 * nr;
            }
        }
        return interval;
    }
}
