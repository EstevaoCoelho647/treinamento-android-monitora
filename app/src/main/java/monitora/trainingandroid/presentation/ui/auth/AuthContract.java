package monitora.trainingandroid.presentation.ui.auth;

import monitora.trainingandroid.domain.entity.Status;
import monitora.trainingandroid.domain.entity.User;

/**
 * Created by estevao on 13/01/17.
 */

public interface AuthContract {
    interface View {
        void onLoadStatusTypeComplete(Status.Type statusType);

        void onAuthSuccess(String credential, User entity);

        void showError(String message);
    }

    interface Presenter {
        void setView(AuthContract.View view);

        void loadStatus();

        void callGetUser(String authorization);

        void callAccessToken(String cliId, String cliSecret, String code);
    }
}
