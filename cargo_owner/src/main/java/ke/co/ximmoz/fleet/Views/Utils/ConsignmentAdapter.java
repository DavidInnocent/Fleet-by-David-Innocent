package ke.co.ximmoz.fleet.Views.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

import static ke.co.ximmoz.fleet.Views.FleetRequestsActivity.hasPermissions;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentAdapter.ConsignmentViewHolder> {


    private  Context context;
    private ConsignmentViewmodel consignmentViewmodel;
    private  List<Consignment> consignments;


    public class ConsignmentViewHolder extends RecyclerView.ViewHolder {
        TextView distanceToDestination;
        Button confirmPickup;

        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceToDestination=itemView.findViewById(R.id.distanceToDestination);
            confirmPickup=itemView.findViewById(R.id.confirmPickup);

        }
    }

    public ConsignmentAdapter (Context context, List<Consignment> consignments)
    {
        this.context=context;
        this.consignments=consignments;
    }

    @NonNull
    @Override
    public ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.consignment_list_view,parent,false);
        return new ConsignmentViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {

        Consignment consignment = consignments.get(position);
        holder.distanceToDestination.setText(consignment.getDistance());
        holder.confirmPickup.setOnClickListener(v->
                {
                    Intent intent=new Intent(context,TrackerService.class);
                    intent.putExtra("ConsignmentID",consignment.getId());
                    context.startService(intent);

                }

        );
    }



    @Override
    public int getItemCount() {
        return consignments.size();
    }


}
