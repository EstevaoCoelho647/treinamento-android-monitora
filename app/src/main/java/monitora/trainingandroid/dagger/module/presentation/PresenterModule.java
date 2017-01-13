package monitora.trainingandroid.dagger.module.presentation;

import dagger.Module;
import dagger.Provides;
import monitora.trainingandroid.dagger.module.scope.PerActivity;
import monitora.trainingandroid.domain.entity.repository.GitHubLoginRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubOAuthRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubStatusRepository;
import monitora.trainingandroid.presentation.ui.auth.AuthContract;
import monitora.trainingandroid.presentation.ui.auth.AuthPresenter;

/**
 * Created by estevao on 13/01/17.
 */

@Module
public class PresenterModule {

    @PerActivity
    @Provides
    AuthContract.Presenter provideMainPresenter(
            GitHubLoginRepository gitHubRepository,
            GitHubStatusRepository gitHubStatusRepository,
            GitHubOAuthRepository gitHubOAuthRepository) {
        return new AuthPresenter(gitHubRepository,
                gitHubStatusRepository,
                gitHubOAuthRepository);
    }
}