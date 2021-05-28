package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PendingPatientAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Call<ArrayList<String>> call3 = userApi.getAllRequestCaretaker(gd.getCurrentUser().getUsername());

        call3.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if(response.code() == 200) {
                    gd.getCurrentUser().getTemporaryPatients().clear();
                    for (String s: response.body()){
                        gd.getCurrentUser().getTemporaryPatients().add(new User(s));
                    }
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });

        pendingRequests.addAll(gd.getCurrentUser().getTemporaryPatients());
        adapter = new PendingPatientAdapter(this, R.layout.pending_patient_request, pendingRequests);

        pendingRequestsList.setAdapter(adapter);

        submitBt.setOnClickListener(v -> {
            username = usernameText.getText().toString();

            Call<Void> call = userApi.addRequestCaretaker(gd.getCurrentUser().getUsername(), username);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.code() == 200) {
                        makeToast("Request sent to patient");
                        gd.getCurrentUser().getTemporaryPatients().add(new User(username));
                        refreshList();
                    } else if (response.code() == 400) {
                        makeToast("Error");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
                }
            });

            Call<Void> call2 = userApi.addRequestPatient(username, gd.getCurrentUser().getUsername());

            call2.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.code() == 200) {
                    } else if (response.code() == 400) {
                        makeToast("Error");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
                }
            });

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