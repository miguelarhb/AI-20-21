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
import java.util.Date;
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

        getUserMode(gd.getCurrentUser().getUsername());

        //TODO remove later (test data)
        if (getSharedPreferenceBoolean("MODE")) {

            if (false) {
                gd.getCurrentUser().addPatient(new User("Pedro"));
                gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
                gd.getCurrentUser().addPatient(new User("Ricardo"));

                Medicine med = new Medicine();
                med.setName("med1");
                med.setQuantity(1);
                med.setExpirationDate(new Date());
                med.setNotes("notes");
                //gd.getActivePatient().addMedicine(med);

                Prescription p = new Prescription();
                p.generateId();
                p.setMedicine(new Medicine("Medicine1", 20));
                p.setQuantity(1);
                p.setStartDate(LocalDateTime.now().minusDays(1).plusHours(1));
                p.setEndDate(LocalDateTime.now().plusDays(6));
                p.setPeriodicity("1-Days");
                p.setNotes("Must not die");
                //
                Prescription p2 = new Prescription();
                p2.generateId();
                p2.setMedicine(new Medicine("Medicine2", 5));
                p2.setQuantity(2);
                p2.setStartDate(LocalDateTime.now().minusDays(3).minusHours(1));
                p2.setEndDate(LocalDateTime.now().plusDays(1));
                p2.setPeriodicity("17-Hours");
                p2.setNotes(":)");


            }

        } else {
            //patient test data
            if (false) {
                gd.getCurrentUser().setCaretaker(new User("Carlos"));

                Medicine med = new Medicine();
                med.setName("med1");
                med.setQuantity(1);
                med.setExpirationDate(new Date());
                med.setNotes("notes");
                //gd.getCurrentUser().addMedicine(med);

                Prescription p = new Prescription();
                p.generateId();
                p.setMedicine(new Medicine("Medicine1", 20));
                p.setQuantity(1);
                p.setStartDate(LocalDateTime.now().minusDays(1).plusHours(1));
                p.setEndDate(LocalDateTime.now().plusDays(6));
                p.setPeriodicity("1-Days");
                p.setNotes("Must not die");
                //gd.getCurrentUser().addPrescription(p, getApplicationContext());
                //
                Prescription p2 = new Prescription();
                p2.generateId();
                p2.setMedicine(new Medicine("Medicine2", 5));
                p2.setQuantity(2);
                p2.setStartDate(LocalDateTime.now().minusDays(3).minusHours(1));
                p2.setEndDate(LocalDateTime.now().plusDays(1));
                p2.setPeriodicity("17-Hours");
                p2.setNotes(":)");
                //gd.getCurrentUser().addPrescription(p2, getApplicationContext());
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
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
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
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
    }

    public void getUserMode(String username){
        Call<ArrayList<String>> call2 = userApi.getAllPatient(username);
        logThis(1);
        call2.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if(response.code() == 200) {
                    logThis("here");
                    if (response.body().size()>0){
                        logThis("here2");
                        writeSharedPreferencesBoolean("MODE", true);
                    }
                    else{
                        writeSharedPreferencesBoolean("MODE",false);
                    }
                } else if (response.code() == 400) {
                    logThis(2);
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                logThis(3);
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
    }
}
