package ke.co.ximmoz.fleet.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.User;
import ke.co.ximmoz.fleet.R;

public class DriverDashboardActitivy extends AppCompatActivity {

    @OnClick(R.id.viewConsignments) void OpenFleetJobs()
    {
        startActivity(new Intent(DriverDashboardActitivy.this,FleetRequestsActivity.class));
    }
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        user=(User)intent.getSerializableExtra("User");


    }
}
