package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface PrescriptionApi {
    @POST("prescription/{user}/add")
    Call<Void> createPrescription(@Path("user") String username, @Body HashMap<String, String> map);

    @GET("prescription/{user}/all")
    Call<ArrayList<Prescription>> getAllPrescription(@Path("user") String username);

    @DELETE("prescription/{user}/delete/{name}")
    Call<Void> deletePrescription(@Path("user") String username, @Path("name") String prescriptionId);

    @PUT("prescription/{user}/edit/{name}")
    Call<Prescription> editPrescription(@Path("user") String username, @Path("name") String prescriptionId, @Body HashMap<String, String> map);
}
