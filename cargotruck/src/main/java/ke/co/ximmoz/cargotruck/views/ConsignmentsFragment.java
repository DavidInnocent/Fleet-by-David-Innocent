package ke.co.ximmoz.cargotruck.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labo.kaji.fragmentanimations.MoveAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.cargotruck.R;
import ke.co.ximmoz.cargotruck.utils.ConsignmentAdapter;
import ke.co.ximmoz.cargotruck.viewmodels.ConsignmentViewmodel;



public class ConsignmentsFragment extends Fragment implements LifecycleOwner {


    private NavController navController;
    private Unbinder unbinder;
    private ConsignmentViewmodel consignmentViewmodel;
    private RecyclerView.LayoutManager layoutManager;
    private ConsignmentAdapter adapter;


    @BindView(R.id.consignmentRecyclerVieww)
    RecyclerView consignmentRecyclerView;

    public ConsignmentsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_consignments_history,container,false);
        navController= Navigation.findNavController(container);
        unbinder= ButterKnife.bind(this,view);
        layoutManager=new LinearLayoutManager(getContext());
        consignmentRecyclerView.setLayoutManager(layoutManager);
        consignmentRecyclerView.setHasFixedSize(true);

        consignmentViewmodel.GetAllConsignments().observe(getViewLifecycleOwner(), consignments -> {

                adapter=new ConsignmentAdapter(getActivity(),consignments,navController);
                consignmentRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter) {
//            return CubeAnimation.create(CubeAnimation.DOWN, enter, 500);
            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}