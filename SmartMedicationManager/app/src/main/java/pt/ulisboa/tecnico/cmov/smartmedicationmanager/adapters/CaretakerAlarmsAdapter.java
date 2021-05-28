package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.CaretakerAlarmsActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class CaretakerAlarmsAdapter extends ArrayAdapter<Alarm> {

    private final Context context;
    private Map<Alarm, Prescription> map;
    private List<Alarm> alarms;
    private int resource;
    private CaretakerAlarmsActivity activity;

    public CaretakerAlarmsAdapter(Context context, int resource, List<Alarm> alarms, Map<Alarm, Prescription> map) {
        super(context, resource, alarms);
        this.alarms = alarms;
        this.context = context;
        this.resource = resource;
        this.map = map;
        this.activity = ((CaretakerAlarmsActivity) context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        Alarm a = getItem(position);
        Prescription p = map.get(a);

        ImageView image = view.findViewById(R.id.iconitem);
        TextView name = view.findViewById(R.id.name);
        TextView qt = view.findViewById(R.id.quantity);
        TextView date = view.findViewById(R.id.alarmDate);
        TextView status = view.findViewById(R.id.status);

        //image.setImageBitmap();
        name.setText(p.getMedicine().getName());
        qt.setText(String.valueOf(p.getQuantity()));
        date.setText(activity.friendlyDateTimeFormat(a.getDateTime()));
        status.setText("Missed");
        status.setTextColor(Color.RED);

        return view;

    }

}
