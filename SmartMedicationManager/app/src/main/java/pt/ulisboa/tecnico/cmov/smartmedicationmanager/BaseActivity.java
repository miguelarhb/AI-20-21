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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.api.MedicineApi;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.api.PrescriptionApi;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.api.UserApi;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.data.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity {

    GlobalData gd;

    Toolbar toolbar;

    //ASSIGN PERSONAL IP HERE "http://<ip>:3000/"
    //static final String BASE_URL = "http://192.168.1.52:3000/";
    static final String BASE_URL = "http://192.168.1.11:3000/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    MedicineApi medicineApi = retrofit.create(MedicineApi.class);
    PrescriptionApi prescriptionApi = retrofit.create(PrescriptionApi.class);
    UserApi userApi = retrofit.create(UserApi.class);

    static String SHARED_PREFERENCES_FILE = "smcprefs";
    static int CAMERA_PERMISSION_CODE = 100;
    public static final String ALARM_ACTION_INTENT = "smc.alarms";
    static int ALARM_CODE = 300;

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
        MenuInflater menuInflater = getMenuInflater();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadToolbar() {
        if (gd == null) {
            gd = (GlobalData) getApplicationContext();
        }

        if (gd.getCurrentUser() == null) {
            logThis("wat");
            gd.setCurrentUser(new User(getSharedPreferenceString("username")));
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

        if (getSharedPreferenceBoolean("MODE")) {
            menu_layout = R.menu.mainmenu_caretaker;
        } else {
            menu_layout = R.menu.mainmenu_patient;
        }
        invalidateOptionsMenu();
    }

    public void makeToast(Object s) {
        Toast.makeText(this, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    public void logThis(Object s) {
        Log.d("fds", String.valueOf(s));
    }

    public void writeSharedPreferencesBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getSharedPreferenceBoolean(String key) {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        Boolean b = sharedPref.getBoolean(key, false);
        return b;
    }

    public void writeSharedPreferencesString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSharedPreferenceString(String key) {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String v = sharedPref.getString(key, "");
        return v;
    }

    public String friendlyDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        return dateTime.format(formatter);
    }

    public void getMedicinesAndPrescriptions(String username, Boolean patient){

        //medicines
        Call<ArrayList<Medicine>> call = medicineApi.getAllMedicine(username);

        call.enqueue(new Callback<ArrayList<Medicine>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Medicine>> call, @NonNull Response<ArrayList<Medicine>> response) {
                if(response.code() == 200) {
                    if (patient){
                        gd.getCurrentUser().setMedicines(response.body());
                    }
                    else{
                        gd.getActivePatient().setMedicines(response.body());
                    }

                } else if (response.code() == 400) {
                    makeToast("Fail Getting Medicine");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Medicine>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });

        //prescriptions
        Call<ArrayList<Prescription>> call2 = prescriptionApi.getAllPrescription(username);

        call2.enqueue(new Callback<ArrayList<Prescription>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Prescription>> call, @NonNull Response<ArrayList<Prescription>> response) {
                if(response.code() == 200) {
                    if (patient){
                        gd.getCurrentUser().setPrescriptions(response.body());
                        for (Prescription p: gd.getCurrentUser().getPrescriptions()){
                            if (!p.getPeriodicity().equals("test")){
                                p.setAlarm(BaseActivity.this);
                            }

                        }
                    }
                    else{
                        gd.getActivePatient().setPrescriptions(response.body());
                    }
                } else if (response.code() == 400) {
                    makeToast("Fail Getting Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Prescription>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }

    public void getUserMode(String username){
        Call<ArrayList<String>> call2 = userApi.getAllPatient(username);
        call2.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if(response.code() == 200) {
                    if (response.body().size()>0){
                        gd.getCurrentUser().getPatients().clear();
                        for (String s : response.body()){
                            gd.getCurrentUser().getPatients().add(new User(s));
                        }
                        gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
                        writeSharedPreferencesBoolean("MODE", true);
                    }
                    else{
                        writeSharedPreferencesBoolean("MODE",false);
                    }
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
                getCaretaker();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }
    public void getCaretaker(){
        Call<String> call = userApi.getCaretaker(gd.getCurrentUser().getUsername());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.code() == 200) {
                    logThis("assign");
                    gd.getCurrentUser().setCaretaker(new User(response.body()));
                } else if (response.code() == 400) {
                    logThis("caretaker error");
                } else if (response.code() == 204) {
                    logThis("no assigned caretaker");
                }

                getData();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                logThis("fail caretaker");
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }
    public void getData(){
        if (getSharedPreferenceBoolean("MODE")){

            getMedicinesAndPrescriptions(gd.getActivePatient().getUsername(), false);

        }
        else{
            getMedicinesAndPrescriptions(gd.getCurrentUser().getUsername(), true);
        }
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
