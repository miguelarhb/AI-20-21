package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @POST("user/create")
    Call<Void> createUserPost(@Body HashMap<String, String> map);

    @POST("user/login")
    Call<User> loginUserPost(@Body HashMap<String, String> map);

    @POST("user/addPatient")
    Call<Void> addPatient(@Query("user") String username, @Query("patient") String usernamePatient);

    @POST("user/addCaretaker")
    Call<Void> addCaretaker(@Query("user") String username, @Query("caretaker") String usernameCaretaker);

    @POST("user/addRequestCaretaker")
    Call<Void> addRequestCaretaker(@Query("user") String username, @Query("caretaker") String usernameCaretaker);

    @POST("user/addRequestPatient")
    Call<Void> addRequestPatient(@Query("user") String username, @Query("patient") String usernamePatient);

    @DELETE("user/deletePatient")
    Call<Void> deletePatient(@Query("user") String username, @Query("patient") String usernamePatient);

    @DELETE("user/deleteCaretaker")
    Call<Void> deleteCaretaker(@Query("user") String username);

    @DELETE("user/deleteRequestCaretaker")
    Call<Void> deleteRequestCaretaker(@Query("user") String username, @Query("caretaker") String usernameCaretaker);

    @DELETE("user/deleteRequestPatient")
    Call<Void> deleteRequestPatient(@Query("user") String username, @Query("patient") String usernamePatient);

    @GET("user/getAllPatient")
    Call<ArrayList<String>> getAllPatient(@Query("user") String username);

    @GET("user/getCaretaker")
    Call<ArrayList<String>> getCaretaker(@Query("user") String username);

    @GET("user/getAllRequestCaretaker")
    Call<ArrayList<String>> getAllRequestCaretaker(@Query("user") String username);

    @GET("user/getAllRequestPatient")
    Call<ArrayList<String>> getAllRequestPatient(@Query("user") String username);
}
