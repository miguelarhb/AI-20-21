package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;

public class AlarmActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        loadToolbar();
    }
}