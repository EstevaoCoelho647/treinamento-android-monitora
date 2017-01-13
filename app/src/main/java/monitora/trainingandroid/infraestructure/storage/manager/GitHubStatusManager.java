package monitora.trainingandroid.infraestructure.storage.manager;

import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.repository.GitHubStatusRepository;
import monitora.trainingandroid.infraestructure.storage.service.GitHubStatusService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by estevao on 13/01/17.
 */

public class GitHubStatusManager implements GitHubStatusRepository {

    private final GitHubStatusService mGitHubStatusService;

    public GitHubStatusManager(GitHubStatusService gitHubStatusService) {
        mGitHubStatusService = gitHubStatusService;
    }

    @Override
    public Observable<Status> lastMessage() {
        return mGitHubStatusService.lastMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}