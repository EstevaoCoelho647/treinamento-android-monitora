package monitora.trainingandroid.dagger.module.infraestructure;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monitora.trainingandroid.domain.entity.repository.GitHubLoginRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubOAuthRepository;
import monitora.trainingandroid.domain.entity.repository.GitHubStatusRepository;
import monitora.trainingandroid.infraestructure.storage.manager.GitHubManager;
import monitora.trainingandroid.infraestructure.storage.manager.GitHubOAuthManager;
import monitora.trainingandroid.infraestructure.storage.manager.GitHubStatusManager;
import monitora.trainingandroid.infraestructure.storage.service.GitHubOAuthService;
import monitora.trainingandroid.infraestructure.storage.service.GitHubService;
import monitora.trainingandroid.infraestructure.storage.service.GitHubStatusService;

/**
 * Created by estevao on 13/01/17.
 */

@Module
public class ManagerModule {

    @Singleton
    @Provides
    GitHubLoginRepository providesGitHubRepository(
            GitHubService gitHubService) {
        return new GitHubManager(gitHubService);
    }

    @Singleton
    @Provides
    GitHubStatusRepository providesGitHubStatusRepository(
            GitHubStatusService gitHubStatusService) {
        return new GitHubStatusManager(gitHubStatusService);
    }

    @Singleton
    @Provides
    GitHubOAuthRepository providesGitHubOAuthRepository(
            GitHubOAuthService gitHubOAuthService) {
        return new GitHubOAuthManager(gitHubOAuthService);
    }

}