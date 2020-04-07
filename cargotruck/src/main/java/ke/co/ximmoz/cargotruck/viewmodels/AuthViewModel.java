package ke.co.ximmoz.cargotruck.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ke.co.ximmoz.cargotruck.models.User;
import ke.co.ximmoz.cargotruck.repositories.AuthRepository;


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
