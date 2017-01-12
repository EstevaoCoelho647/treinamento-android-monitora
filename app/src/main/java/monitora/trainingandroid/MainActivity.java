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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

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
import monitora.trainingandroid.util.MySubscriber;
import okhttp3.Credentials;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

        bindRxValidation();

        sharedPreferences = getSharedPreferences(getString(R.string.sp_key_file), MODE_PRIVATE);
    }

    private void bindRxValidation() {
        RxTextView.textChanges(mLayoutTxtPassword.getEditText())
                .skip(1)
                .subscribe(text -> {
                    AppUtil.validateRequiredFields(this, mLayoutTxtPassword);
                });
        RxTextView.textChanges(mLayoutTxtUsername.getEditText())
                .skip(1)
                .subscribe(text -> {
                    AppUtil.validateRequiredFields(this, mLayoutTxtUsername);
                });
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
                String clientId = getString(R.string.oauth_client_id);
                String clientIdSecret = getString(R.string.oauth_client_secret);
                gitHubOAuthApi.accessToken(clientId, clientIdSecret, code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MySubscriber<AccessToken>() {
                            @Override
                            public void onError(String message) {
                                Snackbar.make(buttonOAuth, message, Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(AccessToken accessToken) {
                                sharedPreferences.edit()
                                        .putString(getString(R.string.sp_accessToken), accessToken.getAuthCredential())
                                        .apply();
                                Snackbar.make(buttonOAuth, accessToken.access_token, Snackbar.LENGTH_SHORT).show();
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
            gitHubApi.basicAuth(credential).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySubscriber<User>() {
                        @Override
                        public void onError(String e) {
                            Snackbar.make(view, "Requisition error", Snackbar.LENGTH_SHORT).show();
                            Log.i(TAG, e);
                        }

                        @Override
                        public void onNext(User user) {
                            Log.i(TAG, user.getLogin());
                            sharedPreferences.edit()
                                    .putString(getString(R.string.sp_credential_key), credential)
                                    .apply();
                            Snackbar.make(view, user.getLogin(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        changeColorItens(Status.Type.NONE);
        statusApiImpl.lastMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<Status>() {
                    @Override
                    public void onError(String e) {
                        changeColorItens(Status.Type.MINOR);
                    }

                    @Override
                    public void onNext(Status status) {
                        changeColorItens(Status.Type.GOOD);
                    }
                });
        processOAuthRedirectUri();
    }

    private void changeColorItens(Status.Type type) {
        txtViewServerStatus.setText(type.getName());
        txtViewServerStatus.setTextColor(type.getColor());
    }

}
