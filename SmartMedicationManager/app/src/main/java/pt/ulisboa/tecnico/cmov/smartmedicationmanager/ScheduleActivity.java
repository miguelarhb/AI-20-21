package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.ScheduleAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class ScheduleActivity extends BaseActivity {

    List<Alarm> shownAlarms = new ArrayList<>();
    Map<String, List<Alarm>> map = new TreeMap<>();
    Map<Alarm,Prescription> prescriptionMap = new HashMap<>();
    ScheduleAdapter adapter;
    ListView listView;
    int counter = -1;
    Date now;
    TextView scheduleDate;
    String pickedDate;
    int mYear;
    int mMonth;
    int mDay;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        loadToolbar();

        now = new Date();

        scheduleDate = findViewById(R.id.scheduleDate);
        listView = findViewById(R.id.scheduleList);
        ImageButton previous = findViewById(R.id.previous);
        ImageButton next = findViewById(R.id.next);

        boolean patient = getIntent().getBooleanExtra("patient", false);

        List<Prescription> source;

        if (patient){
            username = gd.getCurrentUser().getUsername();
            getMedicinesAndPrescriptions(gd.getCurrentUser().getUsername(), true);
            source = gd.getCurrentUser().getPrescriptions();
        }
        else{
            username = gd.getActivePatient().getUsername();
            getMedicinesAndPrescriptions(gd.getActivePatient().getUsername(), false);
            source = gd.getActivePatient().getPrescriptions();
        }

        String dateString;
        for (Prescription p: source){
            for (Alarm a : p.getAlarms()){
                dateString = toCalendarString(a.getDateTime());
                if (map.get(dateString)==null){
                    List<Alarm> alarms = new ArrayList<>();
                    alarms.add(a);
                    prescriptionMap.put(a,p);
                    map.put(dateString, alarms);
                }
                else{
                    map.get(dateString).add(a);
                    prescriptionMap.put(a,p);
                }

            }
        }
        for (List l : map.values()){
            l.sort(Comparator.comparing(Alarm::getDateTime));
        }

        adapter = new ScheduleAdapter(this, R.layout.caretaker_alarm_list_item, shownAlarms, prescriptionMap);
        listView.setAdapter(adapter);

        if (prescriptionMap.size()==0) {
            scheduleDate.setText("No items");
        }

        try {
            iterateMap();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        previous.setOnClickListener(v -> {
            counter--;
            if (counter<0){
                counter=map.size()-1;
            }
            try {
                iterateMap();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        next.setOnClickListener(v -> {
            counter++;
            if (counter>=map.size()){
                counter=0;
            }
            try {
                iterateMap();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        scheduleDate.setOnClickListener(v -> {
            datePicker();
        });
    }

    private void iterateMap() throws ParseException {
        int i = 0;
        for (String day : map.keySet()){
            if (counter==-1){
                Date converted = toDate(day);
                if (converted.after(now)){
                    shownAlarms.clear();
                    shownAlarms.addAll(map.get(day));
                    counter=i;
                    scheduleDate.setText(day);
                    refreshList();
                    return;
                }
            }
            else{
                if (i == counter){
                    shownAlarms.clear();
                    shownAlarms.addAll(map.get(day));
                    scheduleDate.setText(day);
                    refreshList();
                    return;
                }
            }
            i++;
        }

    }

    private String toCalendarString(LocalDateTime day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return day.format(formatter);
    }

    private Date toDate(String s) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(s);

    }

    public void refreshList() {
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }

    private void datePicker(){

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

                        pickedDate= toCalendarString(LocalDateTime.of(mYear,mMonth,mDay,0,0));
                        if (map.containsKey(pickedDate)){
                            shownAlarms.clear();
                            shownAlarms.addAll(map.get(pickedDate));
                            scheduleDate.setText(pickedDate);
                            refreshList();
                        }
                        else{
                            makeToast("No records found on that day");
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}