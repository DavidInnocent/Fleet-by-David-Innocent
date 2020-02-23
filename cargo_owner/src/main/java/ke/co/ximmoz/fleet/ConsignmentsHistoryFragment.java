package ke.co.ximmoz.fleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.models.Consignment;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.Utils.ConsignmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsignmentsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsignmentsHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NavController navController;
    private Unbinder unbinder;
    private ConsignmentViewmodel consignmentViewmodel;


    @BindView(R.id.consignmentRecyclerView)
    RecyclerView consignmentRecyclerView;

    private ConsignmentAdapter adapter;


    public ConsignmentsHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsignmentsHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsignmentsHistoryFragment newInstance(String param1, String param2) {
        ConsignmentsHistoryFragment fragment = new ConsignmentsHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_consignments_history,container,false);
        navController= Navigation.findNavController(container);
        unbinder= ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

        consignmentViewmodel.GetOwnerConsignments().observe(getViewLifecycleOwner(), new Observer<List<Consignment>>() {
            @Override
            public void onChanged(List<Consignment> consignments) {
                adapter=new ConsignmentAdapter(getContext(),consignments);
                consignmentRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
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