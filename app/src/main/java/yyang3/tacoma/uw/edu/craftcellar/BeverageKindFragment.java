package yyang3.tacoma.uw.edu.craftcellar;
/*
 * Craft Cellar: Beverage Kind Fragment
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * The main fragment that lists the categories of the cellars.
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class BeverageKindFragment extends Fragment {
    /**This is a string tag */
    public static final String USER = "User";

    private allBeverageInteractionListener allCellar;

    /**
     * Required empty public constructor
     */
    public BeverageKindFragment() {

    }

    /**
     * {@inheritDoc}
     *
     * Gets the user email and username, then adds an onClicklistener to show cellar list.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beverage_kind, container, false);
        String user = null;
        try {
            InputStream emailReader = getActivity().
                    openFileInput(getString(R.string.LOGIN_USERNAME));
            if (emailReader != null) {
                InputStreamReader temp = new InputStreamReader(emailReader);
                BufferedReader theReader = new BufferedReader(temp);

                StringBuilder t = new StringBuilder();
                while ((user = theReader.readLine()) != null) {
                    t.append(user);
                }
                emailReader.close();
                user = t.toString();
                Toast.makeText(getActivity(), user, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            TextView greeting = (TextView) v.findViewById(R.id.display_header);
            greeting.setText("Welcome back " + user + "!");
        }
        Button allBeverage = (Button) v.findViewById(R.id.all);
        allBeverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCellar.allBeverageList();
            }
        });
        return v;
    }

    /**
     * {@inheritDoc}
     *
     * Checks if our listener is attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof allBeverageInteractionListener) {
            allCellar = (allBeverageInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement allBeverageInteractionListener");
        }
    }

    /**
     * Listener to the "all beverage" button.
     */
    public interface allBeverageInteractionListener {
        /** show all beverage list. */
        public void allBeverageList();
    }

}
