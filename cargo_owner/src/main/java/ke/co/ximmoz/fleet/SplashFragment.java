package ke.co.ximmoz.fleet;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.views.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private NavController navController;


    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_splash,container,false);
        unbinder= ButterKnife.bind(this,view);
         navController= Navigation.findNavController(container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();


        new Handler().postDelayed(() -> {
            if(currentUser==null)
            {

                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment());

            }
            else{

                navController.navigate(SplashFragmentDirections.actionSplashFragmentToDashboardFragment());
//                if(currentUser.isEmailVerified())
//                {
//                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToDashboardFragment());
//                }
//                else
//                {
////
//                  currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                      @Override
//                      public void onSuccess(Void aVoid) {
//                          Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
//                      }
//                  }).addOnFailureListener(new OnFailureListener() {
//                      @Override
//                      public void onFailure(@NonNull Exception e) {
//                          Toast.makeText(getActivity(), "Faile", Toast.LENGTH_SHORT).show();
//                      }
//                  });
//
//                }

            }

        }, 3000);


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
