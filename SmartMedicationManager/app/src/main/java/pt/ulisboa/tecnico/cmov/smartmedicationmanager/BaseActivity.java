package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class BaseActivity extends AppCompatActivity {

    GlobalData gd;

    Toolbar toolbar;

    static String SHARED_PREFERENCES_FILE = "smcprefs";
    static int CAMERA_PERMISSION_CODE=100;

    public int menu_layout = R.menu.mainmenu_patient;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuAdvancedMode:
                intent = new Intent(this, AdvancedModeActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuManagePatients:
                intent = new Intent(this, ManagePatientsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuSettings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void loadToolbar(){
        if (gd==null){
            gd = (GlobalData) getApplicationContext();
        }

        if (gd.getCurrentUser()==null){
            gd.setCurrentUser(new User("Paulo"));
        }
        if (gd.getCurrentUser().getPatients().size()==0){
            gd.getCurrentUser().addPatient(new User("someone"));
            gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
            gd.getCurrentUser().addPatient(new User("second"));
        }
        if (gd.userHasPatients()){
            if (gd.getActivePatient().getMedicines().size()==0){
                Medicine med = new Medicine();
                med.setName("med1");
                med.setQuantity(1);
                med.setExpirationDate(new Date());
                med.setNotes("notes");
                gd.getActivePatient().addMedicine(med);
            }
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if (getSharedPreference("MODE")){
            menu_layout = R.menu.mainmenu_caretaker;
            invalidateOptionsMenu();
        }
    }
    public void makeToast(Object s){
        Toast.makeText(this, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    public void logThis(Object s){
        Log.d("fds", String.valueOf(s));
    }

    public void writeSharedPreferences(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILE,Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public Boolean getSharedPreference(String key){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        Boolean b = sharedPref.getBoolean(key, false);
        return b;
    }

    public String friendlyDateTimeFormat(LocalDateTime dateTime){
        return dateTime.getDayOfMonth()
                + "-" + dateTime.getMonth().getValue()
                + "-" + dateTime.getYear()
                + " " + dateTime.getHour()
                + ":" + dateTime.getMinute();
    }
}