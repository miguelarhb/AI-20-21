package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.api.UserApi;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userApi = retrofit.create(UserApi.class);

        // UI handle
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button createBtn = (Button) findViewById(R.id.createBtn);

        EditText usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        EditText passwordTxt = (EditText) findViewById(R.id.passwordTxt);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("username", usernameTxt.getText().toString());
                map.put("password", passwordTxt.getText().toString());

                loginUser(map);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("username", usernameTxt.getText().toString());
                map.put("password", passwordTxt.getText().toString());

                createUser(map);
            }
        });
    }

    private void loginUser(HashMap<String, String> map) {

        Call<User> call = userApi.loginUserPost(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Login User Successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoginActivity.this, "No User Found", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUser(HashMap<String, String> map) {

        Call<Void> call = userApi.createUserPost(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Toast.makeText(LoginActivity.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "User Creation Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

