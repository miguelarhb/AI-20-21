package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.api.UserApi;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.data.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private UserApi userApi;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gd = (GlobalData) getApplicationContext();

        String loggedInUser = getSharedPreferenceString("username");
        if (!loggedInUser.equals("")) {
            gd.setCurrentUser(new User(loggedInUser));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            makeToast("Welcome back " + loggedInUser);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        userApi = retrofit.create(UserApi.class);

        // UI handle
        Button loginBtn = findViewById(R.id.loginBtn);
        Button createBtn = findViewById(R.id.createBtn);

        EditText usernameTxt = findViewById(R.id.usernameTxt);
        EditText passwordTxt = findViewById(R.id.passwordTxt);

        // TODO remove later (na entrega)
        usernameTxt.setText("Paulo");
        passwordTxt.setText("123456");

        loginBtn.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();

            map.put("username", usernameTxt.getText().toString());
            map.put("password", passwordTxt.getText().toString());

            loginUser(map);
        });

        createBtn.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();

            map.put("username", usernameTxt.getText().toString());
            map.put("password", passwordTxt.getText().toString());

            createUser(map);
        });
    }

    private void loginUser(HashMap<String, String> map) {

        Call<User> call = userApi.loginUserPost(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                    gd.setCurrentUser(new User(username));
                    writeSharedPreferencesString("username", username);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 404) {
                    Toast.makeText(LoginActivity.this, "No User Found", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(HashMap<String, String> map) {

        Call<Void> call = userApi.createUserPost(map);

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
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

