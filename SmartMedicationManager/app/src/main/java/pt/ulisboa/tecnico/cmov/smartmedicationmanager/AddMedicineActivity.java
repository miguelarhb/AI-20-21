package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;


public class AddMedicineActivity extends BaseActivity {
    TextView barcodeTxt;
    String barcode;
    Medicine med;

    String[] months = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        loadToolbar();

        EditText nameTxt = findViewById(R.id.addMedName);
        EditText quantityTxt = findViewById(R.id.addMedQuantity);
        Spinner monthSpinner = findViewById(R.id.addMedMonthSpinner);
        EditText yearTxt = findViewById(R.id.addMedYear);
        EditText notesTxt = findViewById(R.id.addMedNotes);

        barcodeTxt = findViewById(R.id.addMedBarcodeView);

        Button barcodeScanBt = findViewById(R.id.addMedBarcodeBt);
        Button submitBt = findViewById(R.id.addMedSubmit);
        Button cancelBt = findViewById(R.id.addMedCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", -1);
        if (mode==-1){
            med = new Medicine();
            quantityTxt.setText("1");
            monthSpinner.setSelection(Calendar.getInstance().get(Calendar.MONTH));
            yearTxt.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }
        else{
            med = gd.getActivePatient().getMedicines().get(mode);
            nameTxt.setText(med.getName());
            quantityTxt.setText(String.valueOf(med.getQuantity()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(med.getExpirationDate());
            monthSpinner.setSelection(cal.get(Calendar.MONTH));
            yearTxt.setText(String.valueOf(cal.get(Calendar.YEAR)));
            notesTxt.setText(med.getNotes());
            barcode=med.getBarcode();
            barcodeTxt.setText("Barcode: " + barcode);
        }

        submitBt.setOnClickListener(v -> {
            String newName = nameTxt.getText().toString();
            Medicine matchingObject = gd.getActivePatient().getMedicines().stream().
                    filter(p -> p.getName().equals(newName)).
                    findAny().orElse(null);
            if (matchingObject!=null && matchingObject!=med){
                makeToast("Medicine name not unique");
                return;
            }
            med.setName(newName);
            med.setBarcode(barcode);
            med.setQuantity(Integer.parseInt(quantityTxt.getText().toString()));
            String s = monthSpinner.getSelectedItem().toString()+", "+yearTxt.getText().toString();
            DateFormat format = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            med.setExpirationDate(date);
            med.setNotes(notesTxt.getText().toString());
            if (mode==-1){
                gd.getActivePatient().addMedicine(med);
            }
            else{
                gd.getActivePatient().getMedicines().set(mode, med);
            }
            this.finish();

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

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
