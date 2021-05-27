package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlarmApi {
    @POST("alarm/add")
    Call<Void> createAlarm(@Query("user") String username, @Query("name") String prescriptionName, @Body Alarm alarm);

    @GET("alarm/all")
    Call<ArrayList<Alarm>> getAllAlarm(@Query("user") String username, @Query("name") String prescriptionName);

    @DELETE("alarm/delete")
    Call<Void> deleteAlarm(@Query("user") String username, @Query("name") String prescriptionName);
}
