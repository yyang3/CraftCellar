package yyang3.tacoma.uw.edu.craftcellar;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeverageAddFragment extends Fragment {

    private static String URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/addBeverage.php?";

    private EditText myBrand;
    private EditText myTitle;
    private EditText myStyle;
    private EditText myYear;
    private EditText myAlcohol;
    private EditText myType;
    private EditText myDescription;
    private EditText myLocation;

    private AddListener mListener;



    public BeverageAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beverage_add, container, false);
        myBrand = (EditText) v.findViewById(R.id.detail_brand_add);
        myTitle = (EditText) v.findViewById(R.id.detail_title_add);
        myStyle = (EditText) v.findViewById(R.id.detail_style_add);
        myYear = (EditText) v.findViewById(R.id.detail_year_add);
        myAlcohol = (EditText) v.findViewById(R.id.detail_abv_add);
        myType = (EditText) v.findViewById(R.id.detail_type_add);
        myDescription = (EditText) v.findViewById(R.id.detail_description_add);
        myLocation = (EditText) v.findViewById(R.id.detail_location_add);
        Button add = (Button) v.findViewById(R.id.add_confirm);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = URLBuilder();
                if (!url.isEmpty()) {
                    mListener.add(url);
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.hide();
        return v;
    }



    /**
     * helper method to build up url for php
     * @return
     */
    private String URLBuilder () {
        StringBuilder sb = new StringBuilder(URL);
        String result = "";
        try {
            InputStream emailReader = getActivity().
                    openFileInput(getString(R.string.LOGIN_FILE));
            if (emailReader != null) {
                InputStreamReader temp = new InputStreamReader(emailReader);
                BufferedReader theReader = new BufferedReader(temp);

                StringBuilder t = new StringBuilder();
                while ((result = theReader.readLine()) != null) {
                    t.append(result);
                }
                emailReader.close();
                result = t.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append("email=");
        sb.append(result);
        sb.append("&cmd=");
        sb.append("Cellar");
        sb.append("&choice=");
        sb.append("add");
        sb.append("&brand=");
        if (myBrand.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter brand",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myBrand.getText().toString());
        };
        sb.append("&title=");
        if (myTitle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter title",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myTitle.getText().toString());
        };
        sb.append("&style=");
        if (myStyle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter style",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myStyle.getText().toString());
        };
        sb.append("&year=");

        if (myYear.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter year",Toast.LENGTH_LONG).show();
            return "";
        }else {
            int year = -1;
            try {
                 year= Integer.parseInt(myYear.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(),"Not an integer",Toast.LENGTH_LONG).show();
                return "";
            }
            Calendar t = Calendar.getInstance();
            if (year > t.get(Calendar.YEAR)) {
                Toast.makeText(getActivity(),"Years should be in the future",Toast.LENGTH_LONG).show();
                return "";
            } else {
                sb.append(myYear.getText().toString());
            }
        };

        sb.append("&percentage=");
        if (myAlcohol.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter percentage",Toast.LENGTH_LONG).show();
            return "";
        }else {
            int percentage = -1;
            try {
                percentage= Integer.parseInt(myAlcohol.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(),"Not an integer",Toast.LENGTH_LONG).show();
                return "";
            }
            if (percentage > 100 || percentage < 0) {
                Toast.makeText(getActivity(),"Percentage should be between 0 and 100",Toast.LENGTH_LONG).show();
                return "";
            } else {
                sb.append(myAlcohol.getText().toString());
            }
        };
        sb.append("&type=");
        if (myType.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter type",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myType.getText().toString());
        };
        sb.append("&description=");
        if (myDescription.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter description",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myDescription.getText().toString());
        };
        sb.append("&location=");
        if (myLocation.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Need to enter location",Toast.LENGTH_LONG).show();
            return "";
        } else {
            sb.append(myLocation.getText().toString());
        };

        String url = sb.toString();
        return url;
    }
    /**
     * {@inheritDoc}
     *
     * Checks if our listener is attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AddListener) {
            mListener = (AddListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement allBeverageInteractionListener");
        }
    }


    /**
     * Listener for the add button
     */
    public interface AddListener {

        public void add(String url);
    }


}
