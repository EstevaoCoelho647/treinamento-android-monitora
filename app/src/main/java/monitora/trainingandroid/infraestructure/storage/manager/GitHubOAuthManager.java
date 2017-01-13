package monitora.trainingandroid.infraestructure.storage.manager;

import monitora.trainingandroid.domain.entity.AccessToken;
import monitora.trainingandroid.domain.entity.repository.GitHubOAuthRepository;
import monitora.trainingandroid.infraestructure.storage.service.GitHubOAuthService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by estevao on 13/01/17.
 */


public class GitHubOAuthManager implements GitHubOAuthRepository {

    private final GitHubOAuthService mGitHubOAuthService;

    public GitHubOAuthManager(GitHubOAuthService gitHubOAuthService) {
        mGitHubOAuthService = gitHubOAuthService;
    }

    @Override
    public Observable<AccessToken> accessToken(String clientId, String clientSecret, String code) {
        return mGitHubOAuthService.accessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}