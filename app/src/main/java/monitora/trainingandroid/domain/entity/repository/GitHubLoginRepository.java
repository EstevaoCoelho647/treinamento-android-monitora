package monitora.trainingandroid.domain.entity.repository;

import monitora.trainingandroid.domain.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by estevao on 11/01/17.
 */

public interface GitHubLoginRepository {

    Observable<User> getUser(String auth);

}
