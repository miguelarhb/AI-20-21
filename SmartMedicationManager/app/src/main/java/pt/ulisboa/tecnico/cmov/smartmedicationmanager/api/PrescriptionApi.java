package pt.ulisboa.tecnico.cmov.smartmedicationmanager.api;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PrescriptionApi {
    @POST("prescription/add")
    Call<Void> createPrescription(@Query("user") String username, @Body Prescription prescription);

    @GET("prescription/all")
    Call<ArrayList<Prescription>> getAllPrescription(@Query("user") String username);

    @DELETE("prescription/delete")
    Call<Void> deletePrescription(@Query("user") String username, @Query("name") String prescriptionName);

    @PUT("prescription/edit")
    Call<Void> editPrescription(@Query("user") String username, @Query("name") String prescriptionId, @Body Prescription prescription);
}
