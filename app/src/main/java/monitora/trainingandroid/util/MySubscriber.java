package monitora.trainingandroid.util;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by estevao on 12/01/17.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {
    private static final String TAG = MySubscriber.class.getSimpleName();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, e.getMessage());
        onError(e.getMessage());
    }

    public abstract void onError(String message);
}
