package monitora.trainingandroid.domain.entity;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by estevao on 11/01/17.
 */

public interface GitHubApi {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Observable<User> basicAuth(@Header("Authorization") String auth);

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build();
}
