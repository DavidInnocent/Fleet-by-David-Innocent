package ke.co.ximmoz.fleet.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ximmoz.fleet.Models.User;




public class AuthRepository {


    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference();
    MutableLiveData<User> registeredUser=new MutableLiveData<>();



    public MutableLiveData<FirebaseUser> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        final MutableLiveData<FirebaseUser> authenticatedUser=new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {


                     authenticatedUser.setValue(firebaseAuth.getCurrentUser());

                }
                else{
                    authenticatedUser.setValue(null);
                }

            }
        });
        return authenticatedUser;
    }

    public MutableLiveData<User> createUserInDb(final User userr)
    {
        final MutableLiveData<User> authUser=new MutableLiveData<>();
        databaseRef.child("Users").child(userr.getuid()).setValue(userr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    authUser.setValue(userr);
                }
            }
        });

        return authUser;
    }

    public LiveData<User> checkIfUserHasAlreadyRegisteredAsDriver(FirebaseUser user) {

        databaseRef.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                     registeredUser.setValue(dataSnapshot.getValue(User.class));
                }
                registeredUser.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                registeredUser.setValue(null);
            }
        });
        return registeredUser;
    }
}
