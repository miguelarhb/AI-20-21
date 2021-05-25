package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class User {
    //unique
    private String username;

    private String password;

    private User caretaker;

    private List<User> patients = new ArrayList<>();

    private List<Medicine> medicines = new ArrayList<>();

    private List<Prescription> schedule = new ArrayList<>();

    private List<User> requests = new ArrayList<>();

    private List<User> temporaryPatients = new ArrayList<>();

    //

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCaretaker() {
        return caretaker;
    }

    public void setCaretaker(User caretaker) {
        this.caretaker = caretaker;
    }

    public List<User> getPatients() {
        return patients;
    }

    public void setPatients(List<User> patients) {
        this.patients = patients;
    }

    public void addPatient(User patient) {
        this.patients.add(patient);
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public void addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
    }

    public List<Prescription> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Prescription> schedule) {
        this.schedule = schedule;
    }

    public List<User> getRequests() {
        return requests;
    }

    public void setRequests(List<User> requests) {
        this.requests = requests;
    }

    public List<User> getTemporaryPatients() {
        return temporaryPatients;
    }

    public void setTemporaryPatients(List<User> temporaryPatients) {
        this.temporaryPatients = temporaryPatients;
    }

    //

    public void addPrescription(Prescription prescription, Context context) {
        this.schedule.add(prescription);
        prescription.generateAlarms();
        prescription.setAlarm(context);

    }

    public void deletePrescription(Prescription prescription, Context context){
        prescription.cancelAlarm(context);
        this.schedule.remove(prescription);
        prescription.getAlarms().clear();
    }

    public void updatePrescription(int index, Prescription prescription, Context context){
        this.schedule.set(index, prescription);
        prescription.generateAlarms();
        prescription.setAlarm(context);
    }
}
