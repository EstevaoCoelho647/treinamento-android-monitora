package monitora.trainingandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monitora.trainingandroid.domain.entity.AccessToken;
import monitora.trainingandroid.domain.entity.GitHubApi;
import monitora.trainingandroid.domain.entity.GitHubOAuthApi;
import monitora.trainingandroid.domain.entity.GitHubStatusApi;
import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.User;
import monitora.trainingandroid.util.AppUtil;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";
    @BindView(R.id.txt_view_server_status)
    protected TextView txtViewServerStatus;
    @BindView(R.id.tilUsername)
    protected TextInputLayout mLayoutTxtUsername;
    @BindView(R.id.tilPassword)
    protected TextInputLayout mLayoutTxtPassword;
    @BindView(R.id.buttonAuth)
    protected Button buttonOAuth;
    private SharedPreferences sharedPreferences;
    private GitHubOAuthApi gitHubOAuthApi;
    private GitHubStatusApi statusApiImpl;
    private GitHubApi gitHubApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        bindButtonAuth();

        statusApiImpl = GitHubStatusApi.retrofit.create(GitHubStatusApi.class);
        gitHubApi = GitHubApi.retrofit.create(GitHubApi.class);
        gitHubOAuthApi = GitHubOAuthApi.retrofit.create(GitHubOAuthApi.class);

        sharedPreferences = getSharedPreferences(getString(R.string.sp_key_file), MODE_PRIVATE);
    }

    private void bindButtonAuth() {
        buttonOAuth.setOnClickListener(view -> {
            final String baseUrl = GitHubOAuthApi.BASE_URL + "authorize";
            final String clientId = getString(R.string.oauth_client_id);
            final String redirectUri = getOAuthRedirectUri();
            final Uri uri = Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void processOAuthRedirectUri() {
        // Os intent-filter's permitem a interação com o ACTION_VIEW
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(this.getOAuthRedirectUri())) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                //TODO Pegar o access token (Client ID, Client Secret e Code)
                String clientId = getString(R.string.oauth_client_id);
                String clientIdSecret = getString(R.string.oauth_client_secret);
                gitHubOAuthApi.accessToken(clientId, clientIdSecret, code).enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response.isSuccessful()) {
                            AccessToken accessToken = response.body();
                            sharedPreferences.edit()
                                    .putString(getString(R.string.sp_accessToken), accessToken.getAuthCredential())
                                    .apply();
                            Snackbar.make(buttonOAuth, accessToken.access_token, Snackbar.LENGTH_SHORT).show();
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Snackbar.make(buttonOAuth, error, Snackbar.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {

                    }
                });
            } else if (uri.getQueryParameter("error") != null) {
                //TODO Tratar erro
            }
            // Limpar os dados para evitar chamadas múltiplas
            getIntent().setData(null);
        }
    }

    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    @OnClick(R.id.buttonLogin)
    protected void bindButtonLogin(View view) {
        boolean can = AppUtil.validateRequiredFields(MainActivity.this, mLayoutTxtUsername, mLayoutTxtPassword);
        if (can) {
            final String credential = Credentials.basic(mLayoutTxtUsername.getEditText().getText().toString(), mLayoutTxtPassword.getEditText().getText().toString());
            gitHubApi.basicAuth(credential).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, response.body().getLogin());
                        sharedPreferences.edit()
                                .putString(getString(R.string.sp_credential_key), credential)
                                .apply();
                        Snackbar.make(view, response.body().getLogin(), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "Error of server");
                        Snackbar.make(view, "Requisition error", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Snackbar.make(view, "Requisition error", Snackbar.LENGTH_SHORT).show();
                    Log.i(TAG, t.getMessage());
                }
            });
        }

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
        processOAuthRedirectUri();
    }

    private void changeColorItens(String name, int color) {
        txtViewServerStatus.setText(name);
        txtViewServerStatus.setTextColor(color);
    }

}
