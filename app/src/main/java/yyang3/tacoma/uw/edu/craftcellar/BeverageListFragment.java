package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: Beverage List Fragment
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;


/**
 * This class is our fragment for showing the list of beverages the user has.
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class BeverageListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private static final String BEVERAGE_URL = "http://cssgate.insttech." +
            "washington.edu/~tbraden/user_php/test.php?cmd=Cellar";
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeverageListFragment() {
    }


    /**
     * {@inheritDoc}
     * Also checks the column count
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * {@inheritDoc}
     * gets the user email and builds the php string.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverage_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView = recyclerView;
        }

        DownloadBeverageTask task = new DownloadBeverageTask();
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
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = this.getArguments();
        String type = bundle.getString("Kind");
        String temp = BEVERAGE_URL + "&email=" + result +  "&type=" + type;
        Log.i("ptt", temp);
        task.execute(new String[]{temp});

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

    /**
     * {@inheritDoc}
     * makes sure the context is an onlistfragmentlistener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * removes the listener.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Beverage item);
    }

    /**
     * This class downloads all of the beverages that will be populated into out list
     * fragment.
     */
    private class DownloadBeverageTask extends AsyncTask<String, Void, String> {

        /**
         * {@inheritDoc}
         * connects with the server and builds our result string.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "unable to download the " +
                            "list of beverages in the cellar, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * {@inheritDoc}
         * creates our beverage list and loads in into our list recyclerview.
         */
        @Override
        protected void onPostExecute(String result) {

            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_SHORT).show();
                return;
            }
            List<Beverage> beverageList = new ArrayList<>();
            result = Beverage.parseBeveragesJSON(result, beverageList);

            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }

            if (!beverageList.isEmpty()) {
                mRecyclerView.setAdapter(new MyBeverageRecyclerViewAdapter(beverageList, mListener));
            }

        }
    }
}
