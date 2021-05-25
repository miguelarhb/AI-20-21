package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MedicineApi {
    @POST("item/add")
    Call<Void> createMedicine(@Query("user") String username, @Body Medicine medicine);

    @GET("item/all")
    Call<ArrayList<Medicine>> getAllMedicine(@Query("user") String username);

    @DELETE("item/delete")
    Call<Void> deleteMedicine(@Query("user") String username, @Query("name") String medicineName);

    @PUT("item/edit")
    Call<Medicine> editMedicine(@Query("user") String username, @Query("name") String medicineName, @Body Medicine medicine);
}
