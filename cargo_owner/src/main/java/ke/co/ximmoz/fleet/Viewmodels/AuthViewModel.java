package ke.co.ximmoz.fleet.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import ke.co.ximmoz.fleet.Models.User;
import ke.co.ximmoz.fleet.Repositories.AuthRepository;

public class AuthViewModel extends AndroidViewModel {


    private AuthRepository authRepository;
    public LiveData<User> authenticatedUser;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository();
    }


    public LiveData<FirebaseUser> signInWithGoogle(AuthCredential googleAuthCredential) {
        return authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public LiveData<User> saveUserToRTDB(User user) {
        return authRepository.createUserInDb(user);
    }


}
