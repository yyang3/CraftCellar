package yyang3.tacoma.uw.edu.craftcellar;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
//    private SignInListener signInListener;
//    private RegisterListener rListener;

    private final static String LOG_IN_URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/login.php?";
    private EditText mEmail;
    private EditText mPwd;


    private SignInListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = (EditText) v.findViewById(R.id.sign_in_email);
        mPwd = (EditText) v.findViewById(R.id.sign_in_pw);
        Button signInButton = (Button) v.findViewById(R.id.sign_in_confirm);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mEmail.getText().toString();
                String pwd = mPwd.getText().toString();
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter userid"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mEmail.requestFocus();
                    return;
                }
                if (!userId.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwd.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwd.requestFocus();
                    return;
                }

                String url = buildCourseURL(v);
                mListener.SignIn(userId, url);
            }
        });

        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInListener) {
            mListener = (SignInListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement UserRegistrationListener");
        }
    }


    //
    public interface SignInListener {
        public void SignIn(String email, String url);
    }

    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(LOG_IN_URL);

        try {

            String email = mEmail.getText().toString();
            sb.append("email=");
            sb.append(email);


            String pwd = mPwd.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            Log.i("Login", sb.toString());

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

}
