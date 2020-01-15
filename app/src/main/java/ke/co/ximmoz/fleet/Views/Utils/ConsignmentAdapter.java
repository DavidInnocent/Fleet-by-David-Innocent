package ke.co.ximmoz.fleet.Views.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;

public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentAdapter.ConsignmentViewHolder> {


    private  Context context;
    private  List<Consignment> consignments;

    public class ConsignmentViewHolder extends RecyclerView.ViewHolder {
        TextView txtView;
        Button confirmPickup;

        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView=itemView.findViewById(R.id.distanceToDestination);
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
        holder.txtView.setText(consignment.getContainer_size());
        holder.confirmPickup.setOnClickListener(v->{
            Toast.makeText(context.getApplicationContext(), consignment.getId(), Toast.LENGTH_SHORT).show();
                }

        );
    }



    @Override
    public int getItemCount() {
        return consignments.size();
    }


}
