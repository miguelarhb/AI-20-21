package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.PrescriptionListActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class PrescriptionAdapter extends ArrayAdapter<Prescription> {

    private final Context context;
    private List<Prescription> schedule;
    private int resource;
    private PrescriptionListActivity activity;

    public PrescriptionAdapter(Context context, int resource, List<Prescription> schedule) {
        super(context, resource, schedule);
        this.schedule = schedule;
        this.context = context;
        this.resource = resource;
        this.activity = ((PrescriptionListActivity) context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        Prescription p = getItem(position);

        ImageView image = view.findViewById(R.id.iconitem);
        TextView name = view.findViewById(R.id.medicineName);
        TextView qt = view.findViewById(R.id.quantity);
        TextView until = view.findViewById(R.id.untilEndDate);
        TextView periodicity = view.findViewById(R.id.periodicity);
        ImageButton editBt = view.findViewById(R.id.editPrescButton);

        //image.setImageBitmap();
        name.setText(p.getMedicine().getName());
        qt.setText(p.getQuantity() + " pills");
        periodicity.setText("Every "+p.getPeriodicity());
        until.setText("Until "+ activity.friendlyDateTimeFormat(p.getEndDate()));


        editBt.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Edit Medicine");
            alertDialog.setMessage("Edit Or Delete?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "EDIT",
                    (dialog, which) -> {
                        activity.editPrescription(p);
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (dialog, which) -> dialog.cancel());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "DELETE",
                    (dialog, which) -> {
                        activity.deletePrescription(p);
                        dialog.dismiss();
                    });

            alertDialog.show();

        });


        return view;

    }

}
