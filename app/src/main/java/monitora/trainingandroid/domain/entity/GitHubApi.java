package monitora.trainingandroid.domain.entity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by estevao on 11/01/17.
 */

public interface GitHubApi {

    String BASE_URL = "https://api.github.com";

    @GET("/user")
    Call<User> getUser();
}
