package monitora.trainingandroid.infraestructure.storage.service;

import monitora.trainingandroid.domain.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by estevao on 11/01/17.
 */

public interface GitHubService {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Observable<User> basicAuth(@Header("Authorization") String auth);

}
