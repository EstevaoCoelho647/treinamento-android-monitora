package monitora.trainingandroid.presentation.ui.auth;

import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.repository.GitHubLoginRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubOAuthRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubStatusRepository;

/**
 * Created by estevao on 13/01/17.
 */

public class AuthPresenter implements AuthContract.Presenter {
    private AuthContract.View mView;
    private GitHubLoginRepository mGitHubRepository;
    private GitHubStatusRepository mGitHubStatusRepository;
    private GitHubOAuthRepository mGitHubOAuthRepository;

    public AuthPresenter(GitHubLoginRepository gitHubRepository,
                         GitHubStatusRepository gitHubStatusRepository,
                         GitHubOAuthRepository gitHubOAuthRepository) {
        mGitHubRepository = gitHubRepository;
        mGitHubStatusRepository = gitHubStatusRepository;
        mGitHubOAuthRepository = gitHubOAuthRepository;
    }

    @Override
    public void setView(AuthContract.View view) {
        mView = view;
    }

    @Override
    public void loadStatus() {
        mGitHubStatusRepository.lastMessage()
                .subscribe(entity -> {
                    mView.onLoadStatusTypeComplete(entity.getType());
                }, error -> {
                    mView.onLoadStatusTypeComplete(Status.Type.MAJOR);
                });
    }

    @Override
    public void callGetUser(String authorization) {
        mGitHubRepository.getUser(authorization)
                .subscribe(entity -> {
                    mView.onAuthSuccess(authorization, entity);
                }, error -> {
                    mView.showError(error.getMessage());
                });
    }

    @Override
    public void callAccessToken(String clientId,
                                String clientSecret,
                                String code) {
        mGitHubOAuthRepository.accessToken(clientId, clientSecret, code)
                .subscribe(entity -> {
                    callGetUser(entity.getAuthCredential());
                }, error -> {
                    mView.showError(error.getMessage());
                });
    }
}
