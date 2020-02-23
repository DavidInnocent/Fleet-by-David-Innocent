package ke.co.ximmoz.fleet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import javax.xml.validation.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.viewmodels.AuthViewModel;
import ke.co.ximmoz.fleet.views.Utils.FieldsValidator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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


    public LoginFragment() {
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
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    @OnClick(R.id.sign_up_to_sign) void SignUP()
    {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
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
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment());
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
