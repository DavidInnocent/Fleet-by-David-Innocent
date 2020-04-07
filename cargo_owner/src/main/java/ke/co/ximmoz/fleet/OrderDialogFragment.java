package ke.co.ximmoz.fleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.CubeAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.ke.ximmoz.commons.models.Consignment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDialogFragment#} factory method to
 * create an instance of this fragment.
 */
public class OrderDialogFragment extends Fragment {


    private NavController navController;
    private Unbinder unbinder;
    private Consignment consignment;

    @BindView(R.id.refNumber)
    TextView refNumber;

    @BindView(R.id.orderAmount)
    TextView orderAmount;

    @BindView(R.id.finish_sign_up)
    ImageView finish_sign_up;

    public OrderDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment OrderDialogFragment.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consignment=OrderDialogFragmentArgs.fromBundle(getArguments()).getConsignment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order_dialog, container, false);
        // Inflate the layout for this fragment
        unbinder= ButterKnife.bind(this,view);
        navController= NavHostFragment.findNavController(this);
//        getDialog().getWindow().setBackgroundDrawableResource(R.color.red);
//        setCancelable(false);
        return view;
    }


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Dialog dialog=super.onCreateDialog(savedInstanceState);
//        dialog.getWindow().setLayout(5000, 500);
//
//        return dialog;
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refNumber.setText(String.valueOf(consignment.getConsignmentTransactionNumber()));
        orderAmount.setText("Payable Amount \n"+consignment.getAmount());

        finish_sign_up.setOnClickListener(v -> {navController.popBackStack();});
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return CubeAnimation.create(CubeAnimation.LEFT, enter, 500);

//            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        } else {
            return CubeAnimation.create(CubeAnimation.LEFT, enter, 500);
//            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        }
    }


}