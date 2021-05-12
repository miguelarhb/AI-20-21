package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

import android.app.Application;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class GlobalData extends Application {

    private User currentUser;

    private User activePatient;

    //

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getActivePatient() {
        return activePatient;
    }

    public void setActivePatient(User activePatient) {
        this.activePatient = activePatient;
    }

    //

    public boolean userHasPatients(){
        if (currentUser.getPatients().size()!=0){
            return true;
        }
        return false;
    }
    public boolean userHasCaretaker(){
        if (currentUser.getCaretaker()==null){
            return false;
        }
        return true;
    }
}

