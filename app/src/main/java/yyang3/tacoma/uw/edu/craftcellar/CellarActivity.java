package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: Cellar Activity
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;
import yyang3.tacoma.uw.edu.craftcellar.data.EmailDB;

/**
 * This is our main class that nests all of our classes and fragments.
 */
public class CellarActivity extends AppCompatActivity implements RegistrationFragment.
        UserRegistrationListener, LoginFragment.SignInListener,
        BeverageKindFragment.allBeverageInteractionListener, BeverageListFragment.OnListFragmentInteractionListener, BeverageAddFragment.AddListener {


    private SharedPreferences mSharedPreferences;
    private String mEmail;


    /**
     * {@inheritDoc}
     * launches our login fragment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new LoginFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BeverageKindFragment()).addToBackStack(null).commit();
        }

    }

    /**
     * {@inheritDoc}
     * passes our inflate method the menu "menu_cellar"
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cellar, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     * creates our logout feature.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences temp = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            temp.edit().putBoolean(getString(R.string.LOGGEDIN), false).commit();

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new LoginFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * access our registration fragment
     * @param v our current view
     */
    public void toRegister(View v) {
        RegistrationFragment temp = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

    /**
     * {@inheritDoc}
     * creates a new user and then takes you back to login page.
     */
    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * {@inheritDoc}
     * sets the user URL to log the user in.
     */
    @Override
    public void SignIn(final String email, String url) {
        mEmail = email;
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        LoginTask task = new LoginTask();
        task.execute(new String[]{url});

        try {
            OutputStreamWriter userEmailWriter = new OutputStreamWriter(openFileOutput(
                    getString(R.string.LOGIN_FILE), Context.MODE_PRIVATE));
            userEmailWriter.write(email);
            userEmailWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * {@inheritDoc}
     *  creates and opens the beverage list fragment
     */
    @Override
    public void allBeverageList(String Kind) {
        BeverageListFragment temp = new BeverageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Kind", Kind);
        temp.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

    /**
     * {@inheritDoc}
     * replaces current fragment with the beverageDetailFragment
     */
    @Override
    public void onListFragmentInteraction(Beverage item) {

        BeverageDetailFragment beverageDetailFragment = new BeverageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(BeverageDetailFragment.BEVERAGE_ITEM_SELECTED, item);
        beverageDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, beverageDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void add(String url) {
        BeverageAndCellarAddTask temp = new BeverageAndCellarAddTask();
        temp.execute(new String[]{url});
    }


    private class AddUserTask extends AsyncTask<String, Void, String> {

        /**
         * {@inheritDoc}
         * Creates a new user.
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
                    response = "Unable to add user, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "user successfully added!"
                            , Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the add user data" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("User", e.getMessage());

            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private EmailDB mEmailDB;

        /**
         * {@inheritDoc}
         * creates url string for user login then connects to the server to see if
         * user login was successful.
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
                    response = "Unable to log in, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    String username = (String) jsonObject.get("username");
                    setTitle(username + "'s Cellar");

                    if (mEmailDB == null) {
                        mEmailDB = new EmailDB(CellarActivity.this);
                    }
                    mEmailDB.deleteEmail();

                    // Also, add to the local database
                    String email = (String) jsonObject.get("email");
                    mEmailDB.insertEmail(mEmail);
                    List<String> e = mEmailDB.getDB();
                    StringBuilder t = new StringBuilder("Email listed: ");
                    for (String temp : e) {
                        t.append(temp);
                        t.append("; ");
                    }
                    Toast.makeText(CellarActivity.this, t.toString(), Toast.LENGTH_SHORT).show();


                    try {
                        OutputStreamWriter usernameWriter = new OutputStreamWriter(openFileOutput(
                                getString(R.string.LOGIN_USERNAME), Context.MODE_PRIVATE));
                        usernameWriter.write(username);
                        usernameWriter.close();
                    } catch (Exception te) {
                        te.printStackTrace();
                    }
                    Bundle user = new Bundle();
                    user.putString(BeverageKindFragment.USER, username);
                    BeverageKindFragment temp = new BeverageKindFragment();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, temp).addToBackStack(null).commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to log in: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_SHORT)
                            .show();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the log in data" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("User", e.getMessage());

            }
        }
    }
    /**
     * This class is used to update all of the information presented in the view created from
     * this fragment.  This class runs a background thread to the URL that will return the
     * feedback from the server.
     */
    private class BeverageAndCellarAddTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                    response = "Unable to add beverage, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("User", "After do");
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result json return from the php file
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                Log.i("ResultTest", result);
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "successfully add the beverage",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "failed, reason: " + result,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {

            }
        }
    }


}
