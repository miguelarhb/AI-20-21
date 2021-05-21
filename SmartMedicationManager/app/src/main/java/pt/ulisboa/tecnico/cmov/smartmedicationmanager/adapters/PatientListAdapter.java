package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.ManagePatientsActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.data.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class PatientListAdapter extends ArrayAdapter<User> {

    private final Context context;
    private List<User> patients;
    private int resource;
    private ManagePatientsActivity activity;
    private GlobalData gd;

    public PatientListAdapter(Context context, int resource, List<User> patients, GlobalData gd) {
        super(context, resource, patients);
        this.patients=patients;
        this.context=context;
        this.resource=resource;
        this.activity = ((ManagePatientsActivity) context);
        this.gd=gd;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view=inflater.inflate(resource,null);
        }

        User u = getItem(position);

        TextView nameText = view.findViewById(R.id.patientName);
        ImageButton setActiveBt = view.findViewById(R.id.setActive);
        ImageButton deleteBt = view.findViewById(R.id.deletePatient);

        nameText.setText(u.getUsername());

        if (patients.size()==1){
            gd.setActivePatient(patients.get(0));
        }

        if (gd.getActivePatient()!=null){
            if (u.getUsername().equals(gd.getActivePatient().getUsername())){
                setActiveBt.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else {
                setActiveBt.setImageResource(android.R.drawable.checkbox_off_background);
            }
        }

        setActiveBt.setOnClickListener(v -> {
            activity.setActive(u);

        });

        deleteBt.setOnClickListener(v -> {
            activity.delete(u);

        });


        return view;

    }

}
