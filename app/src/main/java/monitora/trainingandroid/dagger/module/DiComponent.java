package monitora.trainingandroid.dagger.module;

import javax.inject.Singleton;

import dagger.Component;
import monitora.trainingandroid.MainActivity;

/**
 * Created by estevao on 12/01/17.
 */

@Singleton
@Component(modules = {
        ApplicationModule.class,
        PreferenceModule.class,
        NetworkModule.class,
        ServiceModule.class
})
public interface DiComponent {
    void inject(MainActivity activity);
}