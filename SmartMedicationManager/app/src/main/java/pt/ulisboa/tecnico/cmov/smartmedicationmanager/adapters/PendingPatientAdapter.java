package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.AddPatientActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class PendingPatientAdapter extends ArrayAdapter<User> {

    private final Context context;
    private List<User> pendingPatients;
    private int resource;
    private AddPatientActivity activity;

    public PendingPatientAdapter(Context context, int resource, List<User> patients) {
        super(context, resource, patients);
        this.pendingPatients=patients;
        this.context=context;
        this.resource=resource;
        this.activity = ((AddPatientActivity) context);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view=inflater.inflate(resource,null);
        }

        User u = getItem(position);

        TextView nameText = view.findViewById(R.id.patientName);
        ImageButton removeBt = view.findViewById(R.id.requestRemove);

        nameText.setText(u.getUsername());

        removeBt.setOnClickListener(v -> {
            activity.remove(u);

        });


        return view;

    }

}
