package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class AddPatientActivity extends BaseActivity {
    EditText usernameText;
    TextView serverCheck;
    Button checkBt;
    Button submitBt;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        loadToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usernameText = findViewById(R.id.addPatientEditText);
        serverCheck = findViewById(R.id.addPatientTextViewCheck);
        checkBt = findViewById(R.id.addPatientCheckBt);
        submitBt = findViewById(R.id.addPatientSubmitBt);

        submitBt.setEnabled(false);

        checkBt.setOnClickListener(v -> {
            username = usernameText.getText().toString();

            if (true) {
                serverCheck.setText("Valid user");
                submitBt.setEnabled(true);
            }
            else{
                serverCheck.setText("User not found");
                submitBt.setEnabled(false);
            }

        });

        submitBt.setOnClickListener(v -> {
            gd.getCurrentUser().addPatient(new User(username));
            finish();


        });


    }
}