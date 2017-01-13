package monitora.trainingandroid.presentation.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monitora.trainingandroid.R;
import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.User;
import monitora.trainingandroid.infraestructure.storage.service.GitHubOAuthService;
import monitora.trainingandroid.presentation.base.BaseActivity;
import monitora.trainingandroid.presentation.helper.AppHelper;
import okhttp3.Credentials;

public class AuthActivity extends BaseActivity implements AuthContract.View {

    protected static final String TAG = "AuthActivity";
    @BindView(R.id.txt_view_server_status)
    protected TextView txtViewServerStatus;
    @BindView(R.id.tilUsername)
    protected TextInputLayout mLayoutTxtUsername;
    @BindView(R.id.tilPassword)
    protected TextInputLayout mLayoutTxtPassword;
    @BindView(R.id.buttonAuth)
    protected Button buttonOAuth;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected AppHelper mAppHelper;
    @Inject
    AuthContract.Presenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.getDaggerUiComponent().inject(this);
        authPresenter.setView(this);
        bindRxValidation();
    }

    private void bindRxValidation() {
        RxTextView.textChanges(mLayoutTxtPassword.getEditText())
                .skip(1)
                .subscribe(text -> {
                    mAppHelper.validateRequiredFields(mLayoutTxtPassword);
                });
        RxTextView.textChanges(mLayoutTxtUsername.getEditText())
                .skip(1)
                .subscribe(text -> {
                    mAppHelper.validateRequiredFields(mLayoutTxtUsername);
                });

        RxView.clicks(buttonOAuth).subscribe(aVoid -> {
            final String baseUrl = GitHubOAuthService.BASE_URL + "authorize";
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
                authPresenter.callAccessToken(clientId, clientIdSecret, code);
            } else if (uri.getQueryParameter("error") != null) {
                showError(uri.getQueryParameter("error"));
            }
            // Limpar os dados para evitar chamadas múltiplas
            getIntent().setData(null);
        }
    }

    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    @OnClick(R.id.buttonLogin)
    protected void bindButtonLogin() {
        boolean can = mAppHelper.validateRequiredFields(mLayoutTxtUsername, mLayoutTxtPassword);
        if (can) {
            final String credential = Credentials.basic(mLayoutTxtUsername.getEditText().getText().toString(), mLayoutTxtPassword.getEditText().getText().toString());
            authPresenter.callGetUser(credential);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeColorItens(Status.Type.NONE);
        authPresenter.loadStatus();
        processOAuthRedirectUri();
    }

    private void changeColorItens(Status.Type type) {
        txtViewServerStatus.setText(type.getName());
        txtViewServerStatus.setTextColor(type.getColor());
    }

    @Override
    public void onLoadStatusTypeComplete(Status.Type statusType) {
        changeColorItens(statusType);
    }

    @Override
    public void onAuthSuccess(String credential, User user) {
        Log.i(TAG, user.getLogin());
        sharedPreferences.edit()
                .putString(getString(R.string.sp_credential_key), credential)
                .apply();
        Snackbar.make(mLayoutTxtUsername, user.getLogin(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Log.i(TAG, message);
        Snackbar.make(mLayoutTxtUsername, message, Snackbar.LENGTH_SHORT).show();
    }
}
