package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.RequestsActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class RequestAdapter extends ArrayAdapter<User> {

    private final Context context;
    private List<User> requests;
    private int resource;
    private RequestsActivity activity;

    public RequestAdapter(Context context, int resource, List<User> requests) {
        super(context, resource, requests);
        this.requests=requests;
        this.context=context;
        this.resource=resource;
        this.activity = ((RequestsActivity) context);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view=inflater.inflate(resource,null);
        }

        User u = getItem(position);

        TextView nameText = view.findViewById(R.id.patientName);
        ImageButton acceptBt = view.findViewById(R.id.accept);
        ImageButton declineBt = view.findViewById(R.id.decline);

        nameText.setText(u.getUsername());

        acceptBt.setOnClickListener(v -> {
            activity.accept(u);

        });

        declineBt.setOnClickListener(v -> {
            activity.decline(u);

        });


        return view;

    }

}
