package ke.co.ximmoz.fleet.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ximmoz.fleet.Models.User;
import ke.co.ximmoz.fleet.R;

public class CargoOwnerActitivy extends AppCompatActivity {

    Button driver,cargoOwner;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_owner);
        Intent intent=getIntent();
        user=(User)intent.getSerializableExtra("User");

    }
}
