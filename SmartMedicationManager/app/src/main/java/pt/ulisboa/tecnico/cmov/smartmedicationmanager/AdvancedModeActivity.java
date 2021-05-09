package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

public class AdvancedModeActivity extends BaseActivity {
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_mode);
        loadToolbar();

        switchCompat = findViewById(R.id.advancedModeSwitch);

        if (getSharedPreference("MODE")){
            switchCompat.setChecked(true);
            switchCompat.setText("On");
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    alertDialog.setMessage("This feature is intended for caretakers only. Do you want to proceed?");
                    alertDialog.setTitle("Warning");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            (dialog, which) -> {
                                switchCompat.setText("On");
                                writeSharedPreferences("MODE", true);
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                            (dialog, which) -> {
                                switchCompat.setText("Off");
                                switchCompat.setChecked(false);
                                writeSharedPreferences("MODE",false);
                                dialog.cancel();
                            });

                    alertDialog.show();
                } else {
                    switchCompat.setText("Off");
                    writeSharedPreferences("MODE",false);
                }
            }
        });
    }
}