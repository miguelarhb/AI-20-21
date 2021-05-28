package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @POST("user/create")
    Call<Void> createUserPost(@Body User user);

    @POST("user/login")
    Call<String> loginUserPost(@Body User user);

    @POST("user/addPatient")
    Call<Void> addPatient(@Query("name") String username, @Query("patient") String usernamePatient);

    @POST("user/addCaretaker")
    Call<Void> addCaretaker(@Query("name") String username, @Query("caretaker") String usernameCaretaker);

    @POST("user/addRequestCaretaker")
    Call<Void> addRequestCaretaker(@Query("name") String username, @Query("caretaker") String usernameCaretaker);

    @POST("user/addRequestPatient")
    Call<Void> addRequestPatient(@Query("name") String username, @Query("patient") String usernamePatient);

    @DELETE("user/deletePatient")
    Call<Void> deletePatient(@Query("name") String username, @Query("patient") String usernamePatient);

    @DELETE("user/deleteCaretaker")
    Call<Void> deleteCaretaker(@Query("name") String username);

    @DELETE("user/deleteRequestCaretaker")
    Call<Void> deleteRequestCaretaker(@Query("name") String username, @Query("caretaker") String usernameCaretaker);

    @DELETE("user/deleteRequestPatient")
    Call<Void> deleteRequestPatient(@Query("name") String username, @Query("patient") String usernamePatient);

    @GET("user/getAllPatient")
    Call<ArrayList<String>> getAllPatient(@Query("name") String username);

    @GET("user/getCaretaker")
    Call<String> getCaretaker(@Query("name") String username);

    @GET("user/getAllRequestCaretaker")
    Call<ArrayList<String>> getAllRequestCaretaker(@Query("name") String username);

    @GET("user/getAllRequestPatient")
    Call<ArrayList<String>> getAllRequestPatient(@Query("name") String username);
}
