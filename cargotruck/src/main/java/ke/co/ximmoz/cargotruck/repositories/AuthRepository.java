package ke.co.ximmoz.cargotruck.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

import ke.co.ximmoz.cargotruck.models.User;


public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();

    /**
     * SIGN IN SECTION
     */


    MutableLiveData<String> signInmessage = new MutableLiveData<>();

    public MutableLiveData<String> SignInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            signInmessage.setValue("1");
        })
                .addOnFailureListener(e -> {
                    signInmessage.setValue(e.getMessage());
                });
        return signInmessage;
    }


    /**
     * SIGN UP WITH EMAIL AND PASSWORD
     */

    MutableLiveData<Boolean> messageString = new MutableLiveData<>();

    public LiveData<Boolean> CreateUserWithEmailAndPassword(User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnSuccessListener(authResult -> {
            FirebaseUser firebaseUser = authResult.getUser();
            UpdateUserProfile(user, firebaseUser);

        })
                .addOnFailureListener(e -> messageString.setValue(false));
        return messageString;
    }

    String message;

    private void UpdateUserProfile(User user, FirebaseUser firebaseUser) {

        UserProfileChangeRequest userProfileChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .build();
        firebaseUser.updateProfile(userProfileChange).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageString.setValue(true);
            } else {
                messageString.setValue(false);
            }

        });

    }

    /**
     * SAVE USER TO FIRESTORE
     */
    public boolean SaveUserToFirestore(User user) {
        AtomicBoolean successMessage = new AtomicBoolean(false);
        firebaseFirestore.collection("Users").document(user.getUid()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                successMessage.set(true);
            } else {
                successMessage.set(false);
            }
        });
        return successMessage.get();
    }

    /**
     * FORGOT PASSWORD
     */

    public LiveData<Boolean> ForgotPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginSuccess.setValue(true);
            } else {
                loginSuccess.setValue(false);
            }
        });
        return loginSuccess;
    }


    /**
     * SIGN OUT
     */


    public void SignOut() {
        firebaseAuth.signOut();
    }

}
