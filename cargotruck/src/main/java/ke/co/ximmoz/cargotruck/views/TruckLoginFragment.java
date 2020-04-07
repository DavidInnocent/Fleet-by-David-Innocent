package ke.co.ximmoz.cargotruck.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.labo.kaji.fragmentanimations.MoveAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ke.co.ximmoz.cargotruck.R;
import ke.co.ximmoz.cargotruck.viewmodels.AuthViewModel;
import ke.co.ximmoz.cargotruck.utils.FieldsValidator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TruckLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TruckLoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters

    private NavController navController;
    private AuthViewModel authViewModel;
    private FieldsValidator fieldsValidator;




    @BindView(R.id.login_to_dashboard)
    ImageView login;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password) EditText
    password;
    private Unbinder unbinder;


    public TruckLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TruckLoginFragment newInstance(String param1, String param2) {
        TruckLoginFragment fragment = new TruckLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldsValidator=new FieldsValidator();
        authViewModel= ViewModelProviders.of(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_login,container,false);
        navController= Navigation.findNavController(container);
        unbinder=ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



    @OnClick(R.id.login_to_dashboard) void SignIn()
    {
        String email_string,pass;
        email_string=email.getText().toString();
        pass=password.getText().toString();


        boolean email_check= fieldsValidator.ValidateFields(email_string);
        boolean password_check=fieldsValidator.ValidateFields(pass);
        if(email_check&&password_check)
        {
            authViewModel.SignInWithEmailAndPassword(email_string,pass).observe(this, s -> {
                if(s!="1") {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    return;
                }
                navController.navigate(R.id.action_truckSplashFragment_to_truckLoginFragment);
            });
        }
        else
        {
            Toast.makeText(getActivity(), "Check all the fields for Accuracy", Toast.LENGTH_LONG).show();
        }


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
