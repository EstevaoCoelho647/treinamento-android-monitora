package monitora.trainingandroid;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by estevao on 12/01/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }
}
