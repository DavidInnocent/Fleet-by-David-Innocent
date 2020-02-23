package ke.co.ximmoz.fleet.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ke.co.ximmoz.fleet.models.User;
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
