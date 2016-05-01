package yyang3.tacoma.uw.edu.craftcellar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CellarActivity extends AppCompatActivity {

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

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new LoginFragment()).commit();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SignIn(View v) {
        BeverageKindFragment temp = new BeverageKindFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

    public void toRegister(View v) {
        RegistrationFragment temp = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }

    public void Register(View v) {
        BeverageKindFragment temp = new BeverageKindFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, temp).addToBackStack(null).commit();
    }
//    @Override
//    public void SignIn () {
//        Toast.makeText(this, "Sign in linked fine",Toast.LENGTH_LONG).show();
//    }
//
//
//    @Override
//    public void Register() {
//        RegistrationFragment temp = new RegistrationFragment();
//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_container, temp).commit();
//    }
}
