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

public interface MedicineApi {
    @POST("item/{user}/add")
    Call<Void> createMedicine(@Path("user") String username, @Body HashMap<String, String> map);

    @GET("item/{user}/all")
    Call<ArrayList<Medicine>> getAllMedicine(@Path("user") String username);

    @DELETE("item/{user}/delete/{name}")
    Call<Void> deleteMedicine(@Path("user") String username, @Path("name") String medicineName);

    @PUT("item/{user}/edit/{name}")
    Call<Medicine> editMedicine(@Path("user") String username, @Path("name") String medicineName, @Body HashMap<String, String> map);
}
