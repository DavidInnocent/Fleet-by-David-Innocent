package ke.co.ximmoz.fleet.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;

public class DatePickerActivity extends AppCompatActivity {

    @BindView(R.id.datePicker) DatePicker datePicker;
    Consignment consignment;
    @OnClick (R.id.continueToLocationPicker) void   destinationPicker(){
        String date=datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear();
        consignment.setDate_of_pickup(date);
        Intent intent=new Intent(DatePickerActivity.this,DestinationChooserActivity.class);
        intent.putExtra("Consignment",consignment);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        consignment=(Consignment)intent.getSerializableExtra("Consignment") ;


    }
}
