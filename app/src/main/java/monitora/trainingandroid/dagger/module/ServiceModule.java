package monitora.trainingandroid.dagger.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monitora.trainingandroid.domain.entity.GitHubApi;
import monitora.trainingandroid.domain.entity.GitHubOAuthApi;
import monitora.trainingandroid.domain.entity.GitHubStatusApi;
import retrofit2.Retrofit;

import static monitora.trainingandroid.dagger.module.NetworkModule.RETROFIT_GITHUB;
import static monitora.trainingandroid.dagger.module.NetworkModule.RETROFIT_GITHUB_OAUTH;
import static monitora.trainingandroid.dagger.module.NetworkModule.RETROFIT_GITHUB_STATUS;

/**
 * Created by estevao on 12/01/17.
 */
@Module
public class ServiceModule {
    @Singleton
    @Provides
    GitHubApi providesGitHub(
            @Named(RETROFIT_GITHUB) Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }

    @Singleton
    @Provides
    GitHubStatusApi providesGitHubStatus(
            @Named(RETROFIT_GITHUB_STATUS) Retrofit retrofit) {
        return retrofit.create(GitHubStatusApi.class);
    }

    @Singleton
    @Provides
    GitHubOAuthApi providesGitHubOAuth(
            @Named(RETROFIT_GITHUB_OAUTH) Retrofit retrofit) {
        return retrofit.create(GitHubOAuthApi.class);
    }
}
