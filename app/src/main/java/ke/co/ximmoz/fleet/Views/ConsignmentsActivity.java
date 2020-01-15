package ke.co.ximmoz.fleet.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

    @BindView(R.id.consignmentRecyclerView)
    RecyclerView consignmentRecyclerView;
    private ConsignmentAdapter adapter;
    ArrayList<Consignment> consignmentsList;
    private ConsignmentViewmodel consignmentViewmodel;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignments);
        unbinder=ButterKnife.bind(this);

        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
        consignmentsList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,1);
        consignmentRecyclerView.setLayoutManager(layoutManager);
        consignmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        consignmentRecyclerView.setAdapter(adapter);


        consignmentViewmodel.GetConsignments().observe(this, consignments ->
        {
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

            if(consignments.size()>0)
            {
                adapter = new ConsignmentAdapter(this, consignments);


                for (Consignment consign :
                        consignments) {
                    consignmentsList.add(consign);

                }
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
        unbinder.unbind();
    }
}
