package monitora.trainingandroid;

import android.app.Application;

import monitora.trainingandroid.dagger.DaggerDiComponent;
import monitora.trainingandroid.dagger.DiComponent;
import monitora.trainingandroid.dagger.UiComponent;
import monitora.trainingandroid.dagger.module.ApplicationModule;


/**
 * Created by estevao on 12/01/17.
 */

public class MyApplication extends Application {
    private DiComponent mDiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mDiComponent = DaggerDiComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public UiComponent getDaggerUiComponent() {
        return mDiComponent.uiComponent();
    }
}
