package monitora.trainingandroid.util;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;

import monitora.trainingandroid.R;

/**
 * Created by estevao on 11/01/17.
 */

public final class AppUtil {

    public static boolean validateRequiredFields(Context context, TextInputLayout... fields) {
        boolean isValid = true;
        for (TextInputLayout f :
                fields) {
            if (f.getEditText() != null){
                EditText editText = f.getEditText();
                if (TextUtils.isEmpty(editText.getText())){
                    f.setErrorEnabled(true);
                    f.setError(context.getString(R.string.txt_required));
                    isValid = false;
                }else {
                    f.setErrorEnabled(false);
                    f.setError(null);
                }
            }else
                throw new RuntimeException("O TextInputLayout deve possuir um editText");
        }

        return isValid;
    }
}
