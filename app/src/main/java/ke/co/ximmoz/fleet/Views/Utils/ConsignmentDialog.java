package ke.co.ximmoz.fleet.Views.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

public class ConsignmentDialog extends DialogFragment {

    TextView topView;
    private Consignment consignment;
    private Button confirmPickup;
    private ConsignmentViewmodel consignmentViewmodel;

    public ConsignmentDialog(Consignment consignment) {
        this.consignment=consignment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topView=view.findViewById(R.id.topS);
        confirmPickup=view.findViewById(R.id.confirmPickup);
        topView.setText(consignment.getId());

        confirmPickup.setOnClickListener((v)->{
            confirmPickup(consignment);
        });

    }

    private void confirmPickup(Consignment consignment) {
        consignment.setStatus("waitingpickup");
        consignmentViewmodel.UpdateConsignment(consignment).observe(this, consignmentt-> {

                if(consignmentt!=null)
                {
                    Toast.makeText(getActivity(), "DOneenenen", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "shidaaa", Toast.LENGTH_SHORT).show();
                }

        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.consignment_dialog,container,false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
    }
}
