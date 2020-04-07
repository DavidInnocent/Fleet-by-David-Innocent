package ke.co.ximmoz.fleet.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.fleet.R;

public class ConsignmentActivity extends AppCompatActivity {


    @BindView(R.id.amountToPay) EditText amountToPay;
    @OnClick(R.id.pickupLocation) void dateChooser()
    {
        Consignment consignment=new Consignment();
        consignment.setAmount(amountToPay.getText().toString());
        consignment.setContainer_size(containerSize.getSelectedItem().toString());
        consignment.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //consignment.setPickup_location(spinner.getSelectedItem().toString());
        Intent intent=new Intent(ConsignmentActivity.this,DestinationChooserActivity.class);
        intent.putExtra("Consignment",consignment);
        startActivity(intent);
    }

    @BindView(R.id.contatinerSize) Spinner containerSize;

    @BindView(R.id.spinner) Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment);
        ButterKnife.bind(this);

    }
}
