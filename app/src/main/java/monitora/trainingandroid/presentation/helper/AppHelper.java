package monitora.trainingandroid.presentation.helper;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;

import monitora.trainingandroid.R;

/**
 * Created by estevao on 11/01/17.
 */

public final class AppHelper {

    private final Context mContext;

    public AppHelper(Context context) {
        mContext = context;
    }

    public boolean validateRequiredFields(TextInputLayout... fields) {
        boolean isValid = true;
        for (TextInputLayout f :
                fields) {
            if (f.getEditText() != null) {
                EditText editText = f.getEditText();
                if (TextUtils.isEmpty(editText.getText())) {
                    f.setErrorEnabled(true);
                    f.setError(mContext.getString(R.string.txt_required));
                    isValid = false;
                } else {
                    f.setErrorEnabled(false);
                    f.setError(null);
                }
            } else
                throw new RuntimeException("O TextInputLayout deve possuir um editText");
        }

        return isValid;
    }
}