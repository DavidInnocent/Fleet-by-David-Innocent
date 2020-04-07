package ke.co.ximmoz.fleet.views.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.fleet.ConsignmentsHistoryFragmentDirections;

import ke.co.ximmoz.fleet.R;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentAdapter.ConsignmentViewHolder> {


    private  Context context;

    private  List<Consignment> consignments;
    private NavController navController;


    public class ConsignmentViewHolder extends RecyclerView.ViewHolder {
        TextView distanceToDestination,Destination,topS,orderid;
        ConstraintLayout consignment_detail;


        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceToDestination=itemView.findViewById(R.id.distanceToDestination);
            Destination=itemView.findViewById(R.id.Destination);
            topS=itemView.findViewById(R.id.topS);
            orderid=itemView.findViewById(R.id.orderid);
            consignment_detail=itemView.findViewById(R.id.consignment_detail);
        }
    }
    public ConsignmentAdapter (Context context, List<Consignment> consignments, NavController frag)
    {
        this.context=context;
        this.consignments=consignments;
        this.navController=frag;
    }

    @NonNull
    @Override
    public ConsignmentAdapter.ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.consignment_list_view,parent,false);
        return new ConsignmentViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {

        Consignment consignment = consignments.get(position);
        holder.distanceToDestination.setText(consignment.getDistance());
        holder.Destination.setText(consignment.getDestinationName());
        holder.topS.setText(consignment.getStatus());


        holder.consignment_detail.setOnClickListener(v -> {
            switch(consignment.getStatus())
            {
                case "PENDING_PAYMENT":
                   navController.navigate(ConsignmentsHistoryFragmentDirections.actionConsignmentsHistoryFragmentToOrderDialogFragment(consignment));
                    break;
                case "PENDING_PICKUP":
                    Toast.makeText(context, "Looking for Driver", Toast.LENGTH_SHORT).show();

                    break;
                case "IN_TRANSIT":
                    navController.navigate(ConsignmentsHistoryFragmentDirections.actionConsignmentsHistoryFragmentToTruckMapsFragment(consignment));

                    break;
                case "ARRIVED":
                    Toast.makeText(context, "Driver Has Finished Trip", Toast.LENGTH_SHORT).show();

                    break;

                default:

                    Intent intent=new Intent(context,TrackerService.class);
                    Bundle bundle=new Bundle();
                   bundle.putString("Consignment",consignment.getId());
                    intent.putExtras(bundle);
                    context.startService(intent);

            }

        });
        holder.orderid.setText(String.valueOf(consignment.getConsignmentTransactionNumber()));
        updateButtonStatus(holder);
//        holder.confirmPickup.setText("Track Truck");
//        holder.confirmPickup.setOnClickListener(v->
//                {
//
//                    Toast.makeText(context, "Payment Pending", Toast.LENGTH_SHORT).show();
////                    Intent intent=new Intent(context,TrackerService.class);
////                    intent.putExtra("ConsignmentID",consignment.getId());
////                    context.startService(intent);
//                }
//
//        );
    }

    private void updateButtonStatus(ConsignmentViewHolder holder) {

    }


    @Override
    public int getItemCount() {
        return consignments.size();
    }

    public void clear(){
        consignments.clear();
    }

    /**Function to return the Data List**/
    public List<Consignment> getOnlineList(){
        return this.consignments;
    }

    /**Function to update the specific Item**/
    public void updateTextView(int position, Consignment newItem){
        consignments.set(position, newItem); //set the item
        notifyItemChanged(position, newItem); //notify the adapter for changes
    }
    public void RemoveTextView(int position){
        consignments.remove(position); //set the item
        notifyItemRemoved(position);
    }

    public void AddedTextView(Consignment conAdded) {
        consignments.add(consignments.size()+1,conAdded);
        notifyDataSetChanged();
    }


    /*Get the position of item inside data list base on the given ID*/
    public int getPositionBaseOnItemID(String theID) {
        int length = consignments.size();
        for (int i = 0; i < length; i ++) {
            if(consignments.get(i).getId().equals(theID)) {
                return i;
            }
        }
        return -1; //Item not found
    }
}