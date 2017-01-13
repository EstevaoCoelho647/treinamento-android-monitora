package monitora.trainingandroid.dagger.module.infraestructure;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monitora.trainingandroid.infraestructure.storage.service.GitHubService;
import monitora.trainingandroid.infraestructure.storage.service.GitHubOAuthService;
import monitora.trainingandroid.infraestructure.storage.service.GitHubStatusService;
import retrofit2.Retrofit;

import static monitora.trainingandroid.dagger.module.infraestructure.NetworkModule.RETROFIT_GITHUB;
import static monitora.trainingandroid.dagger.module.infraestructure.NetworkModule.RETROFIT_GITHUB_OAUTH;
import static monitora.trainingandroid.dagger.module.infraestructure.NetworkModule.RETROFIT_GITHUB_STATUS;

/**
 * Created by estevao on 12/01/17.
 */
@Module
public class ServiceModule {
    @Singleton
    @Provides
    GitHubService providesGitHub(
            @Named(RETROFIT_GITHUB) Retrofit retrofit) {
        return retrofit.create(GitHubService.class);
    }

    @Singleton
    @Provides
    GitHubStatusService providesGitHubStatus(
            @Named(RETROFIT_GITHUB_STATUS) Retrofit retrofit) {
        return retrofit.create(GitHubStatusService.class);
    }

    @Singleton
    @Provides
    GitHubOAuthService providesGitHubOAuth(
            @Named(RETROFIT_GITHUB_OAUTH) Retrofit retrofit) {
        return retrofit.create(GitHubOAuthService.class);
    }
}
