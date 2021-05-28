package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.RequestAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsActivity extends BaseActivity {

    ListView requestsList;
    RequestAdapter adapter;
    List<User> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        loadToolbar();

        requestsList = findViewById(R.id.requestsList);

        Call<ArrayList<String>> call = userApi.getAllRequestPatient(gd.getCurrentUser().getUsername());

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if(response.code() == 200) {
                    gd.getCurrentUser().getRequests().clear();
                    for (String s: response.body()){
                        gd.getCurrentUser().getRequests().add(new User(s));
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

        requests.addAll(gd.getCurrentUser().getRequests());
        adapter = new RequestAdapter(this, R.layout.request_list_item, requests);

        requestsList.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();


    }

    public void refreshList() {
        requests.clear();
        requests.addAll(gd.getCurrentUser().getRequests());
        adapter.notifyDataSetChanged();
    }

    public void accept(User u) {

        Call<Void> onlyCall = userApi.acceptRequestPatient(u.getUsername(), gd.getCurrentUser().getUsername());

        onlyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    gd.getCurrentUser().getRequests().remove(u);
                    gd.getCurrentUser().setCaretaker(u);
                    refreshList();
                    makeToast("Success");
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
        
    }

    public void decline(User u) {

        Call<Void> call = userApi.deleteRequestCaretaker(u.getUsername(), gd.getCurrentUser().getUsername());

        call.enqueue(new Callback<Void>() {
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

        Call<Void> call2 = userApi.deleteRequestPatient(gd.getCurrentUser().getUsername(), u.getUsername());

        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    gd.getCurrentUser().getRequests().remove(u);
                    refreshList();
                    makeToast("Declined");
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
    }
}