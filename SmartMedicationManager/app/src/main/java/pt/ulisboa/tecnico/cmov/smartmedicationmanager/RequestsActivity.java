package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.RequestAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

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
        gd.getCurrentUser().getRequests().remove(u);
        gd.getCurrentUser().setCaretaker(u);
        //u.getTemporary.remove(this)
        //u.addPatient(this)
        //TODO
        refreshList();
    }

    public void decline(User u) {
        gd.getCurrentUser().getRequests().remove(u);
        //u.getTemporary.remove(this)
        //TODO
        refreshList();
    }
}