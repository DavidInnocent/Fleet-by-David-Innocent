package ke.co.ximmoz.cargotruck.utils;

import android.text.TextUtils;

public class FieldsValidator {


    public boolean ValidateFields(String email) {
        if(TextUtils.isEmpty(email)||email.length()<5)
        {
            return false;
        }
        else
        {return true;
        }
    }
}
