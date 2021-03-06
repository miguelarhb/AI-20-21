package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPrescriptionActivity extends BaseActivity {

    Prescription p;
    int mode;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    LocalDateTime start=null;
    LocalDateTime end=null;

    TextView startDatetv;
    TextView endDatetv;

    Button submitBt;

    List<String> periodicityOptions = new ArrayList<>(Arrays.asList("Hours","Days"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        loadToolbar();

        Spinner medSpinner = findViewById(R.id.medSpinner);
        EditText quantityTxt = findViewById(R.id.addPrescQt);
        startDatetv = findViewById(R.id.startDatetv);
        Button startDate = findViewById(R.id.startDate);
        endDatetv = findViewById(R.id.endDatetv);
        Button endDate = findViewById(R.id.endDate);
        EditText periodNumber = findViewById(R.id.periodicityNumber);
        Spinner periodSpinner = findViewById(R.id.periodicitySpinner);
        EditText notes = findViewById(R.id.addPrescNotes);
        submitBt = findViewById(R.id.addMedSubmit);
        Button cancelBt = findViewById(R.id.addMedCancel);

        List<Medicine> medicineList = gd.getActivePatient().getMedicines();
        medicineList.sort(Comparator.comparing(Medicine::getName));

        ArrayAdapter<Medicine> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, medicineList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, periodicityOptions);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter2);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);
        if (mode==-1){
            p = new Prescription();
            p.generateId();
            quantityTxt.setText("1");
            periodNumber.setText("1");
            start = LocalDateTime.now();
            end  = start.plusDays(30);
            startDatetv.setText(friendlyDateTimeFormat(start));
            endDatetv.setText(friendlyDateTimeFormat(end));
        }
        else{
            p = gd.getActivePatient().getPrescriptions().get(mode);
            medSpinner.setSelection(gd.getActivePatient().getMedicines().indexOf(p.getMedicine()));
            quantityTxt.setText(String.valueOf(p.getQuantity()));
            start = p.getStartDate();
            startDatetv.setText(friendlyDateTimeFormat(p.getStartDate()));
            end = p.getEndDate();
            endDatetv.setText(friendlyDateTimeFormat(p.getEndDate()));

            periodNumber.setText(p.getPeriodicity().split("-")[0]);
            if (p.getPeriodicity().split("-")[1].equals("Hours")){
                periodSpinner.setSelection(0);
            }
            else{
                periodSpinner.setSelection(1);
            }
            notes.setText(p.getNotes());


        }

        startDate.setOnClickListener(v -> {
            datePicker(startDatetv, 0);

        });

        endDate.setOnClickListener(v -> {
            datePicker(endDatetv, 1);

        });

        submitBt.setOnClickListener(v -> {
            p.setMedicine((Medicine) medSpinner.getSelectedItem());
            p.setQuantity(Integer.parseInt(quantityTxt.getText().toString()));
            p.setStartDate(start);
            p.setEndDate(end);
            p.setPeriodicity(periodNumber.getText().toString()+"-"+periodSpinner.getSelectedItem().toString());
            p.setNotes(notes.getText().toString());
            if (mode==-1){
                addPrescription();
            }
            else{
                editPrescription();
            }
            this.finish();

        });


        cancelBt.setOnClickListener(v -> this.finish());


    }

    private void addPrescription() {
        String patient = gd.getActivePatient().getUsername();
        p.generateAlarms();
        Call<Void> call = prescriptionApi.createPrescription(patient, p);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("New Prescription Added");
                    gd.getActivePatient().getPrescriptions().add(p);
                    p.setAlarm(getApplicationContext());
                } else if (response.code() == 400) {
                    makeToast("Failed Add Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }

    private void editPrescription() {
        String patient = gd.getActivePatient().getUsername();
        p.cancelAlarm(getApplicationContext());
        p.updateAlarms();
        Call<Void> call = prescriptionApi.editPrescription(patient, p.getId() , p);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("Prescription Edited");
                    gd.getActivePatient().getPrescriptions().set(mode, p);
                    p.setAlarm(getApplicationContext());
                } else if (response.code() == 400) {
                    makeToast("Failed Editing Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }

    private void datePicker(TextView dateTv, int date){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mYear = year;
                        mMonth = monthOfYear+1;
                        mDay = dayOfMonth;

                        timePicker(dateTv, date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void timePicker(TextView dateTv, final int date){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        if (date==0){
                            start = LocalDateTime.of(mYear, mMonth, mDay, mHour, mMinute);
                            dateTv.setText(friendlyDateTimeFormat(start));
                        }
                        else{
                            end = LocalDateTime.of(mYear, mMonth, mDay, mHour, mMinute);
                            dateTv.setText(friendlyDateTimeFormat(end));
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}