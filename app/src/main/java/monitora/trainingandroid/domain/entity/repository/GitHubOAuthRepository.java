package monitora.trainingandroid.domain.entity.repository;

import monitora.trainingandroid.domain.entity.AccessToken;
import rx.Observable;

/**
 * Created by estevao on 11/01/17.
 */

public interface GitHubOAuthRepository {

    Observable<AccessToken> accessToken(
            String clientId,
            String clientSecret,
            String code);

}
