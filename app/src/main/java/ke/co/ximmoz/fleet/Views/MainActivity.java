package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.sql.Driver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.User;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10;
    @OnClick(R.id.SignIn) void signInButton(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    GoogleSignInClient mGoogleSignInClient;
    private AuthViewModel authViewModel;
    GoogleSignInOptions gso;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);

            } catch (Exception e) {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                return;

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authViewModel.signInWithGoogle(credential).observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(final FirebaseUser firebaseUser) {
                if(firebaseUser==null){
                    Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(MainActivity.this,ChooseRole.class);
                User userr=new User(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getUid(),firebaseUser.getPhoneNumber());
                intent.putExtra("User",userr);
                startActivity(intent);
                finish();

            }
        });
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewModel();
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("347829116347-c1fdi20m04hhbk8mmptaclqm3tciimab.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
    }



    private void initViewModel() {
        authViewModel= ViewModelProviders.of(this).get(AuthViewModel.class);
    }



    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {

                        Intent intent=new Intent(MainActivity.this,ChooseRole.class);
                        User userr=new User(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getUid(),"");
                        intent.putExtra("User",userr);
                        startActivity(intent);
        }
    }
}
