package ke.co.ximmoz.fleet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.models.Consignment;

public class ConsignmentFragmentDialog extends DialogFragment {


    private NavController navController;
    private Unbinder unbinder;
    private Consignment consignment;




    @BindView(R.id.pick_location)
    TextView pickup_location;
    @BindView(R.id.destination_location) TextView
    destination_location;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.grand_total)
    TextView grand_total;


    @OnClick(R.id.close) void Dismmiss(){
        getDialog().dismiss();
        navController.navigate(ConsignmentFragmentDialogDirections.actionSampleFragmentDialogToDashboardFragment());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.message_dialog,container,false);
//        navController= Navigation.findNavController(container);
        unbinder= ButterKnife.bind(this,view);
        navController= NavHostFragment.findNavController(this);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        consignment=ConsignmentFragmentDialogArgs.fromBundle(getArguments()).getConsignment();
        pickup_location.setText(consignment.getPickupName());
        destination_location.setText(consignment.getDestinationName());
        distance.setText(consignment.getDistance());
        grand_total.setText("KSH. "+consignment.getAmount());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setGravity(Gravity.NO_GRAVITY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
