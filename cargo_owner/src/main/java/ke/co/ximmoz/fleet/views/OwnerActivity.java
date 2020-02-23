package ke.co.ximmoz.fleet.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.Utils.LocationTrackerAdapter;

public class OwnerActivity extends AppCompatActivity {

    private static final String TAG = OwnerActivity.class.getSimpleName();
    @BindView(R.id.consignmentRecyclerView)
    RecyclerView consignmentRecyclerView;
    private LocationTrackerAdapter adapter;
    private ConsignmentViewmodel consignmentViewmodel;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        ButterKnife.bind(this);  
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
        consignmentViewmodel.GetOwnerConsignments().observe(this, consignments ->
        {


            if(consignments.size()>0)
            {
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
                adapter = new LocationTrackerAdapter(this, consignments);
                consignmentRecyclerView.setLayoutManager(layoutManager);
                RecyclerView.ItemDecoration divider= new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
                consignmentRecyclerView.addItemDecoration(divider);
                consignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                consignmentRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
            else
            {
                Toast.makeText(this, "Failedddddd", Toast.LENGTH_SHORT).show();

//                for (Consignment consign :
//                        consignments) {
//                    consignmentsList.add(consign);
//
//                }
//
//                adapter.notifyDataSetChanged();
            }


        });



    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
