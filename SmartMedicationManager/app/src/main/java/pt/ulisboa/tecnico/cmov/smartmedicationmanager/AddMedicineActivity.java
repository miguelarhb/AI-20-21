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
            med = gd.getCurrentUser().getMedicines().get(mode);
            nameTxt.setText(med.getName());
            quantityTxt.setText(String.valueOf(med.getQuantity()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(med.getExpirationDate());
            monthSpinner.setSelection(cal.get(Calendar.MONTH));
            yearTxt.setText(String.valueOf(cal.get(Calendar.YEAR)));
            notesTxt.setText(med.getNotes());
            barcodeTxt.setText(med.getBarcode());
        }

        submitBt.setOnClickListener(v -> {
            med.setName(nameTxt.getText().toString());
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
                gd.getCurrentUser().addMedicine(med);
            }
            else{
                gd.getCurrentUser().getMedicines().set(mode, med);
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

//        getDateBt.setOnClickListener(v -> {
//            DialogFragment newFragment = new DatePickerFragment();
//            newFragment.show(getSupportFragmentManager(), "datePicker");
//
//        });


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

                med.setBarcode(barcode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(year, month, day);
//            Date x = calendar.getTime();
//            expDateTxt.setText(x.toString());
//            finalDate=x;
//        }
//    }


}
