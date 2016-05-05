package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: Login Fragment
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * this class is for accessesing/logging users into the application.
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class LoginFragment extends Fragment {

    private final static String LOG_IN_URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/login.php?";
    private EditText mEmail;
    private EditText mPwd;


    private SignInListener mListener;

    /** Required empty public constructor */
    public LoginFragment() {

    }

    /**
     * {@inheritDoc}
     * Gets the entered text in the EditText field and stores it, then after the button is
     * clicked the email and password are checked to see if they pass all requirements and finally
     * the URL is sent to the signIn method.
     */
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

                String url = buildLoginURL(v);
                mListener.SignIn(userId, url);
            }
        });

        return v;

    }

    /**
     * {@inheritDoc}
     * checks to make sure the context is a listener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInListener) {
            mListener = (SignInListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement UserRegistrationListener");
        }
    }

    /**
     * Listener for the sign in button
     */
    public interface SignInListener {
        /**
         * Signs the user in
         * @param email string version of the user email
         * @param url string version of the destination url
         */
        public void SignIn(String email, String url);
    }

    /**
     * Builds our string needed to send to the server with login information.
     * @param v information regarding the view
     * @return our url for interacting with our php code.
     */
    private String buildLoginURL(View v) {

        StringBuilder sb = new StringBuilder(LOG_IN_URL);

        try {

            String email = mEmail.getText().toString();
            sb.append("email=");
            sb.append(email);


            String pwd = mPwd.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));


        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

}
