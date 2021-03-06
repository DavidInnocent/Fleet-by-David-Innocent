package ke.co.ximmoz.fleet.views.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.ConsignmentTrackerActivity;

public class LocationTrackerAdapter extends RecyclerView.Adapter<LocationTrackerAdapter.LocationTrackerViewHolder> {


    private  Context context;
    private ConsignmentViewmodel consignmentViewmodel;
    private  List<Consignment> consignments;



    public class LocationTrackerViewHolder extends RecyclerView.ViewHolder {
        TextView distanceToDestination;
        Button confirmPickup;

        public LocationTrackerViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceToDestination=itemView.findViewById(R.id.distanceToDestination);
            confirmPickup=itemView.findViewById(R.id.confirmPickup);

        }
    }

    public LocationTrackerAdapter(Context context, List<Consignment> consignments)
    {
        this.context=context;
        this.consignments=consignments;

    }

    @NonNull
    @Override
    public LocationTrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.consignment_list_view,parent,false);
        return new LocationTrackerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull LocationTrackerViewHolder holder, int position) {

        Consignment consignment = consignments.get(position);
        holder.distanceToDestination.setText(consignment.getDistance());
        holder.confirmPickup.setOnClickListener(v->
                {


                            Intent intent =new Intent(context,ConsignmentTrackerActivity.class);
                            intent.putExtra("Consignment",consignment);
                            context.startActivity(intent);



                }

        );
    }




    @Override
    public int getItemCount() {
        return consignments.size();
    }


}
