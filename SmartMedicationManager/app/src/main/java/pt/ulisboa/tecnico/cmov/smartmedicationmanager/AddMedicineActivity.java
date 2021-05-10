package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;
import java.util.Date;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;


public class AddMedicineActivity extends BaseActivity {
    TextView barcodeTxt;
    String barcode;
    Medicine newMed;
    public static TextView expDateTxt;
    public static Date finalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        loadToolbar();

        newMed = new Medicine();

        EditText nameTxt = findViewById(R.id.addMedName);
        EditText quantityTxt = findViewById(R.id.addMedQuantity);
        expDateTxt = findViewById(R.id.addMedDateTxt);
        Button getDateBt = findViewById(R.id.addMedDate);
        EditText notesTxt = findViewById(R.id.addMedNotes);

        barcodeTxt = findViewById(R.id.addMedBarcodeView);

        Button barcodeScanBt = findViewById(R.id.addMedBarcodeBt);
        Button submitBt = findViewById(R.id.addMedSubmit);
        Button cancelBt = findViewById(R.id.addMedCancel);

        quantityTxt.setText("1");

        submitBt.setOnClickListener(v -> {
            newMed.setName(nameTxt.getText().toString());
            newMed.setQuantity(Integer.parseInt(quantityTxt.getText().toString()));
            newMed.setExpirationDate(finalDate);
            newMed.setNotes(notesTxt.getText().toString());
            gd.getCurrentUser().addMedicine(newMed);
            this.finish();

        });

        getDateBt.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");

        });

        cancelBt.setOnClickListener(v -> this.finish());

        barcodeScanBt.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(AddMedicineActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(AddMedicineActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);
            } else {
                new IntentIntegrator(this).initiateScan();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new IntentIntegrator(this).initiateScan();
            } else {
                Toast.makeText(AddMedicineActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                makeToast("Barcode Scanned successfully");
                barcode = result.getContents();

                barcodeTxt.setText("Barcode: " + barcode);

                newMed.setBarcode(barcode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date x = calendar.getTime();
            expDateTxt.setText(x.toString());
            finalDate=x;
        }
    }


}
