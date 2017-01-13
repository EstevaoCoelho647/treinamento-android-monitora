package monitora.trainingandroid.dagger.module.presentation;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import monitora.trainingandroid.presentation.helper.AppHelper;

/**
 * Created by estevao on 13/01/17.
 */

@Module
public class HelperModule {
    @Provides
    @Reusable
    AppHelper provideTextHelper(Context context) {
        return new AppHelper(context);
    }
}