package pt.ulisboa.tecnico.cmov.smartmedicationmanager.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class User {
    //unique
    private String username;

    private String password;

    private List<Medicine> medicines = new ArrayList<>();

    private List<Prescription> prescriptions = new ArrayList<>();

    private User caretaker;

    private List<User> patients = new ArrayList<>();

    private List<User> requests = new ArrayList<>();

    private List<User> temporaryPatients = new ArrayList<>();

    //

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        for (Prescription p : prescriptions){
            Medicine matchingObject = getMedicines().stream().
                    filter(m -> m.getName().equals(p.getItem())).
                    findAny().orElse(null);
            p.setMedicine(matchingObject);
        }
        this.prescriptions = prescriptions;
    }

    //

    public void addPrescription(Prescription prescription, Context context) {
        this.prescriptions.add(prescription);
        prescription.generateAlarms();
        prescription.setAlarm(context);

    }

    public void deletePrescription(Prescription prescription, Context context){
        prescription.cancelAlarm(context);
        prescription.getAlarms().clear();
        this.prescriptions.remove(prescription);
    }

    public void updatePrescription(int index, Prescription prescription, Context context){
        this.prescriptions.set(index, prescription);
        prescription.updateAlarms();
        prescription.setAlarm(context);

    }
}
