package monitora.trainingandroid.domain.entity.repository;

import monitora.trainingandroid.domain.entity.Status;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by estevao on 09/01/17.
 */

public interface GitHubStatusRepository {

    Observable<Status> lastMessage();

}