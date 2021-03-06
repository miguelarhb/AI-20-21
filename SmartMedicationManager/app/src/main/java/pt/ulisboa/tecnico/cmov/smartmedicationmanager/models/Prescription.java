package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.AlarmReceiver;

public class Prescription {

    @SerializedName("name")
    private String id;

    private String item; //medicine name

    private Medicine medicine;

    private int quantity;

    private String periodicity;

    @SerializedName("start")
    private String startDate;

    @SerializedName("end")
    private String endDate;

    private String notes;

    private List<Alarm> alarms = new ArrayList<>();

    public Prescription(Medicine medicine, int quantity, String periodicity, LocalDateTime startDate) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.periodicity = periodicity;
    }

    public Prescription() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.item = medicine.getName();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        return LocalDateTime.parse(startDate, formatter);
    }

    public void setStartDate(LocalDateTime startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        this.startDate = startDate.format(formatter);
    }

    public LocalDateTime getEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        return LocalDateTime.parse(endDate, formatter);
    }

    public void setEndDate(LocalDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        this.endDate = endDate.format(formatter);
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

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    //

    public void generateAlarms(){
        this.alarms.clear();
        long firstAlarm = this.getStartDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long lastThreshold = this.getEndDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long nextAlarm = firstAlarm;
        long interval = getInterval();

        while (nextAlarm <= lastThreshold) {
            Instant instant = Instant.ofEpochMilli(nextAlarm);
            LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            Alarm al = new Alarm();
            al.setDateTime(date);
            al.setTaken(false);
            alarms.add(al);
            nextAlarm += interval;
        }
    }

    //

    public void setAlarm(Context context) {

        long nextAlarm = getNextAlarm();
        if (nextAlarm==0){
            return;
        }

        int alarm_id = this.getId().hashCode();

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);

        myIntent.putExtra("id", this.id);
        myIntent.putExtra("time", nextAlarm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int alarm_id = this.getId().hashCode();
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private long getNextAlarm() {
        if (this.alarms.size()==0){
            generateAlarms();
        }
        long ms;
        for (Alarm alarm: this.alarms){
            ms = alarm.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if ( ms >= System.currentTimeMillis()){
                Log.d("fds", "scheduling "+alarm.getDateTime().toString());
                return ms;
            }
        }
        ms=0;
        return ms;

    }

    public void updateAlarms() {

        ListIterator<Alarm> iterator = alarms.listIterator();
        while (iterator.hasNext()){
            if (iterator.next().getDateTime().isAfter(LocalDateTime.now())) {
                iterator.remove();
            }
        }

        long firstAlarm = this.getStartDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long lastThreshold = this.getEndDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long nextAlarm = firstAlarm;
        long interval = getInterval();

        while (nextAlarm <= lastThreshold) {
            Instant instant = Instant.ofEpochMilli(nextAlarm);
            LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            Alarm al = new Alarm();
            al.setDateTime(date);
            al.setTaken(false);
            alarms.add(al);
            nextAlarm += interval;
        }
    }
}
