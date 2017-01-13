package monitora.trainingandroid.infraestructure.storage.manager;

import monitora.trainingandroid.domain.entity.User;
import monitora.trainingandroid.domain.entity.repository.GitHubLoginRepository;
import monitora.trainingandroid.infraestructure.storage.service.GitHubService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by estevao on 13/01/17.
 */

public class GitHubManager implements GitHubLoginRepository{
    private final GitHubService mGitHubService;

    public GitHubManager(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    @Override
    public Observable<User> getUser(String auth) {
        return mGitHubService.basicAuth(auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
