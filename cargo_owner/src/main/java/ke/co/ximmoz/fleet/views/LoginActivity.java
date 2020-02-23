package ke.co.ximmoz.fleet.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.R;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10;
    @OnClick(R.id.SignIn) void signInButton(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        ButterKnife.bind(this);
        initViewModel();
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("347829116347-c1fdi20m04hhbk8mmptaclqm3tciimab.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }



    private void initViewModel() {

    }



    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {

                        Intent intent=new Intent(LoginActivity.this,ChooseRole.class);
                        //User userr=new User(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getUid(),"");
                        //intent.putExtra("User",userr);
                        startActivity(intent);
        }
    }
}
