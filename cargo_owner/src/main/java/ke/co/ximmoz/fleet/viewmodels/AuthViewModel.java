package ke.co.ximmoz.fleet.viewmodels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import ke.co.ximmoz.fleet.models.User;
import ke.co.ximmoz.fleet.repositories.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;


    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepository();
    }

    public LiveData<Boolean> ForgotPassword(String email) {
        return authRepository.ForgotPassword(email);
    }

    public LiveData<Boolean> CreateUserWithEmailAndPassword(User user) {

        return authRepository.CreateUserWithEmailAndPassword(user);
    }


    public LiveData<String> SignInWithEmailAndPassword(String email, String password) {

        return authRepository.SignInWithEmailAndPassword(email, password);
    }



    public void SignOut() {
        authRepository.SignOut();
    }

}
