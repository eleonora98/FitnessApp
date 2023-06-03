package uni.fmi.project.fitnessapp.service;

import java.util.List;

import kotlin.ParameterName;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import uni.fmi.project.fitnessapp.entity.Training;

public interface TrainingsService {
    @GET("/trainings/single")
    @Headers({"Accept: application/json",
            "Content-type:application/json"})
    Call<Training> getTrainingByName(@Query("name") String name);
}
