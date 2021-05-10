package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

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

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.MedicineListActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private final Context context;
    private List<Medicine> medicines;
    private int resource;
    private MedicineListActivity activity;

    public MedicineAdapter(Context context, int resource, List<Medicine> medicine) {
        super(context, resource, medicine);
        this.medicines = medicine;
        this.context = context;
        this.resource = resource;
        this.activity = ((MedicineListActivity) context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        Medicine m = getItem(position);

        ImageView image = view.findViewById(R.id.iconitem);
        TextView name = view.findViewById(R.id.medicineName);
        TextView qt = view.findViewById(R.id.quantity);
        TextView expDate = view.findViewById(R.id.expirationDate);
        ImageButton editBt = view.findViewById(R.id.editMedicineButton);

        //image.setImageBitmap();
        name.setText(m.getName());
        qt.setText(m.getQuantity() + " box");
        expDate.setText(m.getExpirationDate().toString());

        editBt.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Edit Medicine");
            alertDialog.setMessage("Edit Or Delete?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "EDIT",
                    (dialog, which) -> {
                        activity.editMedicine(m);
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (dialog, which) -> dialog.cancel());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "DELETE",
                    (dialog, which) -> {
                        activity.deleteMedicine(m);
                        dialog.dismiss();
                    });

            alertDialog.show();

        });


        return view;

    }

}
