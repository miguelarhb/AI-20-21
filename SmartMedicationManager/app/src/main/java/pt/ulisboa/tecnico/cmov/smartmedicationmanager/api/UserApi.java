package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("user/create")
    Call<Void> createUserPost(@Body HashMap<String, String> map);

    @POST("user/login")
    Call<User> loginUserPost(@Body HashMap<String, String> map);
}
