package monitora.trainingandroid.dagger;

import javax.inject.Singleton;

import dagger.Component;
import monitora.trainingandroid.dagger.module.ApplicationModule;
import monitora.trainingandroid.dagger.module.PreferenceModule;
import monitora.trainingandroid.dagger.module.infraestructure.ManagerModule;
import monitora.trainingandroid.dagger.module.infraestructure.NetworkModule;
import monitora.trainingandroid.dagger.module.infraestructure.ServiceModule;
import monitora.trainingandroid.dagger.module.presentation.HelperModule;

/**
 * Created by estevao on 12/01/17.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HelperModule.class,
        PreferenceModule.class,
        NetworkModule.class,
        ServiceModule.class,
        ManagerModule.class
})
public interface DiComponent {
    UiComponent uiComponent();
}