package monitora.trainingandroid;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import monitora.trainingandroid.domain.entity.GitHubApi;
import monitora.trainingandroid.domain.entity.GitHubStatusApi;
import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.User;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView txtViewServerStatus;
    private GitHubStatusApi statusApiImpl;
    private GitHubApi gitHubApi;
    private TextInputLayout mLayoutTxtUsername;
    private TextInputLayout mLayoutTxtPassword;
    private Button buttonLogin;
    private Button buttonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindTextStatus();
        bindLayoutUsername();
        bindLayoutPassword();
        bindButtonLogin();
        bindButtonAuth();
        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GitHubStatusApi.BASE_URL)
                .build();
        statusApiImpl = retrofit.create(GitHubStatusApi.class);
    }

    private void bindButtonAuth() {
        buttonAuth = (Button) findViewById(R.id.buttonAuth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void bindButtonLogin() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean can = true;
                if (mLayoutTxtPassword.getEditText().getText().toString().isEmpty()) {
                    mLayoutTxtPassword.setError("Please insert password");
                    can = false;
                }
                if (mLayoutTxtUsername.getEditText().getText().toString().isEmpty()) {
                    mLayoutTxtUsername.setError("Please insert username");
                    can = false;
                }

                if (can) {
                    buildGitHubAPI(mLayoutTxtUsername.getEditText().getText().toString(), mLayoutTxtPassword.getEditText().getText().toString());
                    gitHubApi.getUser().enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Log.i(TAG, response.body().getLogin());
                                Toast.makeText(MainActivity.this, response.body().getLogin(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "Error of server");
                                Toast.makeText(MainActivity.this, "Requisition error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i(TAG, t.getMessage());
                        }
                    });
                }

            }
        });

        mLayoutTxtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLayoutTxtPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLayoutTxtUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLayoutTxtUsername.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void buildGitHubAPI(final String username, final String password) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", Credentials.basic(username, password))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHubApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    private void bindLayoutPassword() {
        mLayoutTxtPassword = (TextInputLayout) findViewById(R.id.tilPassword);
    }

    private void bindLayoutUsername() {
        mLayoutTxtUsername = (TextInputLayout) findViewById(R.id.tilUsername);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeColorItens(Status.Type.NONE.getName(), Status.Type.NONE.getColor());
        statusApiImpl.lastMessage().enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.i(TAG, "Success on request last message");
                if (response.isSuccessful()) {
                    Status status = response.body();
                    changeColorItens(status.getType().getName(), status.getType().getColor());
                } else {
                    try {
                        String string = response.errorBody().string();
                        changeColorItens(string, Status.Type.MINOR.getColor());
                        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.i("MainActivity", "Error on request last message");
                changeColorItens("Error", Status.Type.MINOR.getColor());
            }
        });
    }

    private void changeColorItens(String name, int color) {
        txtViewServerStatus.setText(name);
        txtViewServerStatus.setTextColor(color);
    }

    private void bindTextStatus() {
        txtViewServerStatus = (TextView) findViewById(R.id.txt_view_server_status);
    }
}
