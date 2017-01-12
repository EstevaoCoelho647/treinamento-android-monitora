package monitora.trainingandroid.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monitora.trainingandroid.R;

/**
 * Created by estevao on 12/01/17.
 */

@Module
public class PreferenceModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        final String fileName = context.getString(R.string.sp_key_file);
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
