package monitora.trainingandroid.dagger;

import dagger.Subcomponent;
import monitora.trainingandroid.dagger.module.presentation.PresenterModule;
import monitora.trainingandroid.dagger.module.scope.PerActivity;
import monitora.trainingandroid.presentation.ui.auth.AuthActivity;

/**
 * Created by estevao on 13/01/17.
 */

@PerActivity
@Subcomponent(modules = {PresenterModule.class})
public interface UiComponent {
    void inject(AuthActivity activity);
}