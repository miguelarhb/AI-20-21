package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.data.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gd = (GlobalData) getApplicationContext();

        String loggedInUser = getSharedPreferenceString("username");
//        if (!loggedInUser.equals("")) {
//
//            gd.setCurrentUser(new User(loggedInUser));
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            makeToast("Welcome back " + loggedInUser);
//            startActivity(intent);
//            finish();
//        }

        setContentView(R.layout.activity_login);

        // UI handle
        Button loginBtn = findViewById(R.id.loginBtn);
        Button createBtn = findViewById(R.id.createBtn);

        EditText usernameTxt = findViewById(R.id.usernameTxt);
        EditText passwordTxt = findViewById(R.id.passwordTxt);

        // TODO remove later
        usernameTxt.setText("Paulo");
        passwordTxt.setText("123456");

        loginBtn.setOnClickListener(view -> {
            username = usernameTxt.getText().toString();
            String password = passwordTxt.getText().toString();
            loginUser(new User(username, password));
        });

        createBtn.setOnClickListener(view -> {

            username = usernameTxt.getText().toString();
            String password = passwordTxt.getText().toString();

            createUser(new User(username,password));
        });
    }

    private void loginUser(User u) {

        Call<String> call = userApi.loginUserPost(u);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                    gd.setCurrentUser(new User(username));
                    writeSharedPreferencesString("username", username);
                    getUserMode(username);


                } else if (response.code() == 404) {
                    Toast.makeText(LoginActivity.this, "No User Found", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, Throwable t) {
                makeToast("Login failed");
            }
        });
    }

    private void createUser(User u) {

        Call<Void> call = userApi.createUserPost(u);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 201) {
                    Toast.makeText(LoginActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                makeToast("Create user failed");
            }
        });
    }
}

