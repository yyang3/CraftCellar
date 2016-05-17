package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: Cellar Activity
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;

/**
 * This is our main class that nests all of our classes and fragments.
 */
public class CellarActivity extends AppCompatActivity implements RegistrationFragment.
        UserRegistrationListener, LoginFragment.SignInListener,
        BeverageKindFragment.allBeverageInteractionListener, BeverageListFragment.OnListFragmentInteractionListener {


    private SharedPreferences mSharedPreferences;

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
    public void SignIn(String email, String url) {
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        LoginTask task = new LoginTask();
        task.execute(new String[]{url});

        try {
            OutputStreamWriter userEmailWriter = new OutputStreamWriter(openFileOutput(
                    getString(R.string.LOGIN_FILE), Context.MODE_PRIVATE));
            userEmailWriter.write(email);
            userEmailWriter.close();
            Toast.makeText(this, "Stored in successfully!", Toast.LENGTH_LONG).show();
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
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("User", e.getMessage());

            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

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
                    Toast.makeText(getApplicationContext(), username
                            , Toast.LENGTH_LONG)
                            .show();
                    setTitle(username + "'s Cellar");
                    try {
                        OutputStreamWriter usernameWriter = new OutputStreamWriter(openFileOutput(
                                getString(R.string.LOGIN_USERNAME), Context.MODE_PRIVATE));
                        usernameWriter.write(username);
                        usernameWriter.close();
                        Toast.makeText(CellarActivity.this, "Stored in successfully!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bundle user = new Bundle();
                    user.putString(BeverageKindFragment.USER, username);
                    BeverageKindFragment temp = new BeverageKindFragment();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, temp).addToBackStack(null).commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to log in: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("User", e.getMessage());

            }
        }
    }


}
