package yyang3.tacoma.uw.edu.craftcellar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
//    private SignInListener signInListener;
//    private RegisterListener rListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_login, container, false);
//        Button signin = (Button)v.findViewById(R.id.sign_in_confirm);
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signInListener.SignIn();
//            }
//        });
//        Button re = (Button)v.findViewById(R.id.sign_in_register);
//        re.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rListener.Register();
//            }
//        });
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

//    public interface RegisterListener {
//        public void Register();
//    }
//
//    public interface SignInListener {
//        public void SignIn();
//    }

}
