package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PendingPatientAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class AddPatientActivity extends BaseActivity {
    EditText usernameText;
    TextView serverCheck;
    Button checkBt;
    Button submitBt;

    String username;

    ListView pendingRequestsList;
    PendingPatientAdapter adapter;
    List<User> pendingRequests = new ArrayList<>();

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

        pendingRequestsList = findViewById(R.id.sentRequestsList);

        pendingRequests.addAll(gd.getCurrentUser().getTemporaryPatients());
        adapter = new PendingPatientAdapter(this, R.layout.pending_patient_request, pendingRequests);

        pendingRequestsList.setAdapter(adapter);

        submitBt.setOnClickListener(v -> {
            username = usernameText.getText().toString();
            if (true){
                //update my temp patients
                //update destination user requests
                gd.getCurrentUser().getTemporaryPatients().add(new User(username));
                finish();
            }
            else{
                makeToast("errir");
            }



        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();


    }

    public void refreshList() {
        pendingRequests.clear();
        pendingRequests.addAll(gd.getCurrentUser().getTemporaryPatients());
        adapter.notifyDataSetChanged();
    }

    public void remove(User u) {
        gd.getCurrentUser().getTemporaryPatients().remove(u);
        refreshList();
    }
}