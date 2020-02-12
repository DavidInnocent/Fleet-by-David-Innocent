package ke.co.ximmoz.fleet.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.Views.Utils.ConsignmentAdapter;

public class ConsignmentsActivity extends AppCompatActivity {

    private static final String TAG = ConsignmentsActivity.class.getSimpleName();
    @BindView(R.id.consignmentRecyclerView)
    RecyclerView consignmentRecyclerView;
    private ConsignmentAdapter adapter;
    private static List<Consignment> availableConsignments;
    private ConsignmentViewmodel consignmentViewmodel;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignments);
        ButterKnife.bind(this);  
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
        consignmentViewmodel.GetConsignments().observe(this, consignments ->
        {


            if(consignments.size()>0)
            {

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
                adapter = new ConsignmentAdapter(this, consignments);
                consignmentRecyclerView.setLayoutManager(layoutManager);
                consignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.ItemDecoration divider= new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
                consignmentRecyclerView.addItemDecoration(divider);
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
