package yyang3.tacoma.uw.edu.craftcellar;
/*
 * Craft Cellar: Beverage Detail Fragment
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;


/**
 * This class holds the information for each beverage selected
 * for view from our beverage list.  This class shows the user all
 * information regarding the beverage and also lets the user
 * edit details regarding the beverage.
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class BeverageDetailFragment extends Fragment {
    /**The string for showing currently selected beverage. */
    public static final String BEVERAGE_ITEM_SELECTED = "tag";
    /**the URL used for updating the beverage we have selected. */
    public static final String UPDATE_URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/beverageEdit.php?";
    /**
     * The URL used for deleting the beverage task.
     */
    public static final String DELETE_URL = "http://cssgate.insttech.washington.edu/~tbraden/user_php/removeBeverage.php?";

    private TextView mBrand;
    private TextView mTitle;
    private TextView mStyle;
    private TextView mYear;
    private TextView mDescription;
    private TextView mABV;
    private ImageView mImage;
    private TextView mRate;
    private Beverage mBeverage = null;

    /**
     * This empty constructor is required.
     */
    public BeverageDetailFragment() {

    }

    /**
     * {@inheritDoc}
     *
     * This class locates each TextView and ImageView.
     * added onClick methods to edit our description, image, and rate.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beverage_detail, container, false);
        mBrand = (TextView) view.findViewById(R.id.detail_brand);
        mTitle = (TextView) view.findViewById(R.id.detail_title);
        mStyle = (TextView) view.findViewById(R.id.detail_style);
        mYear = (TextView) view.findViewById(R.id.detail_year);
        mDescription = (TextView) view.findViewById(R.id.detail_description);
        mABV = (TextView) view.findViewById(R.id.detail_abv);
        mImage = (ImageView) view.findViewById(R.id.detail_image);
        mRate = (TextView) view.findViewById(R.id.detail_rating);
        mDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View view = inflater.inflate(R.layout.beverage_update, null);
                TextView temp = (TextView) view.findViewById(R.id.original_data);
                temp.setText(mDescription.getText());

                builder.setView(view)
                        // Add action buttons
                        .setPositiveButton(R.string.edit_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText text = (EditText) view.findViewById(R.id.modified_data);
                                mBeverage.setmDescription(text.getText().toString());
                                mDescription.setText(mBeverage.getmDescription());
                                StringBuilder sb = new StringBuilder(UPDATE_URL);

                                sb.append("email=");
                                sb.append(mBeverage.getMemail());
                                sb.append("&column=");
                                sb.append(Beverage.DESCRIPTION);
                                sb.append("&beverageID=");
                                sb.append(mBeverage.getMid());
                                sb.append("&newData=");
                                sb.append(mBeverage.getmDescription().replaceAll(" ", "%20"));

                                String url = sb.toString();
                                Log.i("URLCHECK", url);

                                BeverageUpdateTask task = new BeverageUpdateTask();
                                task.execute(new String[]{url.toString()});

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.edit_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

        Button delete = (Button) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
                d.setTitle(getString(R.string.delete));
                d.setMessage("Do you really want to delete beverage " +
                        mBeverage.getmTitle() + "from your cellar?");
                d.setPositiveButton(R.string.edit_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder sb = new StringBuilder(DELETE_URL);

                        sb.append("email=");
                        sb.append(mBeverage.getMemail());
                        sb.append(("&cmd=remove"));
                        sb.append("&id=");
                        sb.append(mBeverage.getMid());
                        String url = sb.toString();
                        Log.i("URLCHECK", url);
                        BeverageRemoveTask remove = new BeverageRemoveTask();
                        remove.execute(new String[]{url.toString()});
                        dialog.dismiss();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).setNegativeButton(R.string.edit_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                d.show();
            }
        });
        Button share = (Button) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("Brand", mBeverage.getmBrand());
                b.putString("Title",mBeverage.getmTitle());
                b.putInt("Year",mBeverage.getmByear());

                DialogFragment f = new EmailFragment();
                f.setArguments(b);

                f.show(getActivity().getSupportFragmentManager(),"show");
            }
        });





        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.hide();
        return view;
    }

    /**
     * This method allows the user see all information of the beverage
     * the user selected from the list.
     *
     * @param beverage the beverage being viewed
     * @throws MalformedURLException
     */
    public void updateView(Beverage beverage) throws MalformedURLException {
        mBeverage = beverage;

        if (beverage != null) {
            mBrand.setText(beverage.getmBrand());
            mTitle.setText(beverage.getmTitle());
            mStyle.setText(beverage.getmStyle());
            mYear.setText(Integer.toString(beverage.getmByear()));
            mDescription.setText(beverage.getmDescription());
            mABV.setText(Integer.toString(beverage.getmPercentage()));
            mRate.setText(Integer.toString(beverage.getRate()));
            new ImageLoadTask(beverage.getmImageAdd(), mImage).execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            try {
                updateView((Beverage) args.getSerializable(BEVERAGE_ITEM_SELECTED));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This class is used to remove all of the information presented in the view created from
     * this fragment.  This class runs a background thread to the URL that will return the
     * feedback from the server.
     */
    private class BeverageRemoveTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to remove beverage, Reason: "
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
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Log.i("Beverage Remove", "success");
                } else {
                    Log.i("Beverage Remove", status);
                }
            } catch (JSONException e) {
                Log.i("User", e.getMessage());
            }
        }

    }

    /**
     * This class is used to update all of the information presented in the view created from
     * this fragment.  This class runs a background thread to the URL that will return the
     * feedback from the server.
     */
    private class BeverageUpdateTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to update beverage, Reason: "
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
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Log.i("Beverage Update", "success");
                } else {
                    Log.i("Beverage Update", status);
                }
            } catch (JSONException e) {
                Log.i("User", e.getMessage());
            }
        }
    }

    /**
     * This class is used to get the image and populate on our fragment.
     */
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        /**
         * This method is a constructor for our ImageLoadTask.
         *
         * @param url is the url for the image location
         * @param imageView is our view location on fragment
         */
        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        /**
         * {@inheritDoc}
         * Creates and returns a bitmap based on the image URL
         * @param params the URL
         * @return Bitmap image
         */
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Log.i("in try", url);
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * {@inheritDoc}
         * Sets our image Bitmap
         *
         * @param result the Bitmap image
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }


    }
}
