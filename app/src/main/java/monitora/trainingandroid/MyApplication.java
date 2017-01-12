package monitora.trainingandroid;

import android.app.Application;

import monitora.trainingandroid.dagger.module.ApplicationModule;
import monitora.trainingandroid.dagger.module.DaggerDiComponent;
import monitora.trainingandroid.dagger.module.DiComponent;

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

    public DiComponent getDaggerDiComponent() {
        return mDiComponent;
    }
}
