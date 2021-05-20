package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class GlobalData extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            serviceChannel.enableLights(true);
            serviceChannel.setLightColor(Color.RED);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

