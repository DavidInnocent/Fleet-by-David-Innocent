package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.User;
import ke.co.ximmoz.fleet.R;

public class ChooseRole extends AppCompatActivity {

    private User user;
    @OnClick(R.id.findTruck) void findtruck(){
        user.setIsDriver("no");
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(ChooseRole.this,ConsignmentActivity.class);
                    intent.putExtra("User",user);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(ChooseRole.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @OnClick(R.id.drive) void driver(){

        user.setIsDriver("yes");
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(ChooseRole.this,DriverDashboardActitivy.class);
                    intent.putExtra("User",user);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ChooseRole.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }



            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        user=(User)intent.getSerializableExtra("User");

    }
}
