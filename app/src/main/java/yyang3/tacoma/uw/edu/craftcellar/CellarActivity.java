package yyang3.tacoma.uw.edu.craftcellar;

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

public class CellarActivity extends AppCompatActivity implements RegistrationFragment.
        UserRegistrationListener, LoginFragment.SignInListener,
        BeverageKindFragment.allBeverageInteractionListener, BeverageListFragment.OnListFragmentInteractionListener {


    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cellar, menu);
        return true;
    }

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

//    public void SignIn(View v) {
//        BeverageKindFragment temp = new BeverageKindFragment();
//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
//    }

    public void toRegister(View v) {
        RegistrationFragment temp = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

//    public void Register(View v) {
//        BeverageKindFragment temp = new BeverageKindFragment();
//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
//    }

    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

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

    @Override
    public void allBeverageList() {
        BeverageListFragment temp = new BeverageListFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

    @Override
    public void onListFragmentInteraction(Beverage item) {

    }


    private class AddUserTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            Log.i("User", "Pre");
            super.onPreExecute();
            Log.i("User", "Finish pre");
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.i("User", "do");
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
            Log.i("User", "After do");
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
                    response = "Unable to log in, Reason: "
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
