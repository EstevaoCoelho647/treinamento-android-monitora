package monitora.trainingandroid.presentation.base;

import android.support.v7.app.AppCompatActivity;

import monitora.trainingandroid.MyApplication;
import monitora.trainingandroid.dagger.UiComponent;

/**
 * Created by estevao on 12/01/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }

    protected UiComponent getDaggerUiComponent() {
        return getMyApplication().getDaggerUiComponent();
    }
}
