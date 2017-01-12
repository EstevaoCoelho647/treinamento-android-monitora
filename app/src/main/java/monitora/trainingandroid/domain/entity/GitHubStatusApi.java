package monitora.trainingandroid.domain.entity;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by estevao on 09/01/17.
 */

public interface GitHubStatusApi {

    String BASE_URL = "https://status.github.com/api/";

    @GET("last-message.json")
    Observable<Status> lastMessage();

}