package monitora.trainingandroid.infraestructure.storage.service;

import monitora.trainingandroid.domain.entity.Status;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by estevao on 09/01/17.
 */

public interface GitHubStatusService {

    String BASE_URL = "https://status.github.com/api/";

    @GET("last-message.json")
    Observable<Status> lastMessage();

}