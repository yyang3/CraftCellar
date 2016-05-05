package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: Registration Fragment
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * This fragment collects user information to register new users.
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class RegistrationFragment extends Fragment {
    private UserRegistrationListener mListener;
    private static final String USER_ADD_URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/addUser.php?";
    private EditText mUserName;
    private EditText mUserpw;
    private EditText mUserEmail;

    /** Required empty public constructor */
    public RegistrationFragment() {

    }


    /**
     * {@inheritDoc}
     *
     * this methods gets the User's email, password, and username, then creates a
     * user with the information.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        mUserEmail = (EditText) v.findViewById(R.id.register_email);
        mUserpw = (EditText) v.findViewById(R.id.register_pw);
        mUserName = (EditText) v.findViewById(R.id.register_username);

        Button addCourseButton = (Button) v.findViewById(R.id.register_confirm);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             *
             * sends the URL string made using username, email, and password to buildUserURL.
             */
            @Override
            public void onClick(View v) {
                String url = buildUserURL(v);
                mListener.addUser(url);
            }
        });


        FloatingActionButton b = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        b.hide();
        return v;
    }

    /**
     * {@inheritDoc}
     * makes sure the context is a listener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UserRegistrationListener) {
            mListener = (UserRegistrationListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement UserRegistrationListener");
        }
    }

    /**
     * Listener for the register button
     */
    public interface UserRegistrationListener {
        /**
         * registers a user
         * @param url to send to our php code and create a new user
         */
        public void addUser(String url);
    }

    /**
     * Builds the url needed for the php code to add user to the database.
     * @param v the view and all of the information contained in it.
     * @return string url that will be used to add a user to the database
     */
    private String buildUserURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {

            String email = mUserEmail.getText().toString();
            sb.append("email=");
            sb.append(email);


            String pwd = mUserpw.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));


            String username = mUserName.getText().toString();
            sb.append("&username=");
            sb.append(URLEncoder.encode(username, "UTF-8"));

            Log.i("UserRegistration", sb.toString());

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

}
