package ke.co.ximmoz.fleet.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.Utils.ConsignmentAdapter;

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
//        consignmentViewmodel.GetConsignments().observe(this, consignments ->
//        {
//
//
//            if(consignments.size()>0)
//            {
//
//                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
//                adapter = new ConsignmentAdapter(this, consignments);
//                consignmentRecyclerView.setLayoutManager(layoutManager);
//                consignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                RecyclerView.ItemDecoration divider= new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
//                consignmentRecyclerView.addItemDecoration(divider);
//                consignmentRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//            else
//            {
//                Toast.makeText(this, "Failedddddd", Toast.LENGTH_SHORT).show();
//
////                for (Consignment consign :
////                        consignments) {
////                    consignmentsList.add(consign);
////
////                }
////
////                adapter.notifyDataSetChanged();
//            }
//
//
//        });



    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
