package ke.co.ximmoz.fleet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import javax.annotation.ParametersAreNonnullByDefault;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.models.User;
import ke.co.ximmoz.fleet.viewmodels.AuthViewModel;
import ke.co.ximmoz.fleet.views.Utils.FieldsValidator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements LifecycleOwner {
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


    @BindView(R.id.finish_sign_up)
    ImageView finishSignUp;
    private Unbinder unbinder;

    @BindView(R.id.username)
    EditText usernamee;
    @BindView(R.id.email) EditText emaill;
    @BindView(R.id.phone) EditText phonee;
    @BindView(R.id.password) EditText passwordd;
    @BindView(R.id.password_retry) EditText passRetryy;



    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = ViewModelProviders.of(getActivity()).get(AuthViewModel.class);
        fieldsValidator=new FieldsValidator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_sign_up,container,false);
        navController= Navigation.findNavController(container);
        unbinder= ButterKnife.bind(this,view);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finishSignUp.setOnClickListener(v -> {
            String username,email,phone,password,passRetry;
            username=usernamee.getText().toString();
            email=emaill.getText().toString();
            phone=phonee.getText().toString();
            password=passwordd.getText().toString();
            passRetry=passRetryy.getText().toString();

            boolean username_check=fieldsValidator.ValidateFields(username);
            boolean email_check=fieldsValidator.ValidateFields(email);
            boolean phone_check=fieldsValidator.ValidateFields(phone);
            boolean password_check=fieldsValidator.ValidateFields(password);
            boolean password_retry_check=fieldsValidator.ValidateFields(passRetry);

            if(!password.equals(passRetry))
            {
                Toast.makeText(getActivity(), "Passwords have to match", Toast.LENGTH_SHORT).show();
                return;
            }

            if(username_check&&email_check&&phone_check&&password_check) {

                User user=new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setPhone(phone);
                authViewModel.CreateUserWithEmailAndPassword(user).observe(getViewLifecycleOwner(), s -> {
                    if(!s)
                    {
                        Toast.makeText(getActivity(), "Something Went Wrong Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        FirebaseAuth.getInstance().signOut();
                        navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToDashboardFragment());
                    }
                });

            }
            else
            {
                Toast.makeText(getActivity(),"All fields required to proceed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
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
