package yyang3.tacoma.uw.edu.craftcellar.Beverage;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Beverage implements Serializable {


    private String memail;
    private int mid;
    private String mImageAdd;
    private String mDescription;
    private int rate;
    private String mBrand;
    private String mTitle;
    private String mLocation;
    private int mByear;
    private int mPercentage;
    private String myBtype;
    private String mStyle;

    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String IMAGEADD = "imageadd";
    public static final String DESCRIPTION = "description";
    public static final String RATE = "rate";
    public static final String BRAND = "brand";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String IMAGE = "image";
    public static final String BDES = "bdescription";
    public static final String BYEAR = "Brewyear";
    public static final String BTYPE = "btype";
    public static final String STYLE = "style";
    public static final String PERCENTAGE = "percentage";


    public String getmBrand() {
        return mBrand;
    }

    public void setmBrand(String mBrand) {
        this.mBrand = mBrand;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public int getmByear() {
        return mByear;
    }

    public void setmByear(int mByear) {
        this.mByear = mByear;
    }

    public int getmPercentage() {
        return mPercentage;
    }

    public void setmPercentage(int mPercentage) {
        this.mPercentage = mPercentage;
    }

    public String getMyBtype() {
        return myBtype;
    }

    public void setMyBtype(String myBtype) {
        this.myBtype = myBtype;
    }

    public String getmStyle() {
        return mStyle;
    }

    public void setmStyle(String mStyle) {
        this.mStyle = mStyle;
    }


    public Beverage(String memail, int mid,
                    String mImageAdd, String mDescription, int rate,
                    String mBrand, String mTitle, String mLocation,
                    int mByear, int mPercentage, String myBtype, String mStyle) {
        this.memail = memail;
        this.mid = mid;
        this.mImageAdd = mImageAdd;
        this.mDescription = mDescription;
        this.rate = rate;
        this.mBrand = mBrand;
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mByear = mByear;
        this.mPercentage = mPercentage;
        this.myBtype = myBtype;
        this.mStyle = mStyle;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getmImageAdd() {
        return mImageAdd;
    }

    public void setmImageAdd(String mImageAdd) {
        this.mImageAdd = mImageAdd;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public static String parseBeveragesJSON(String beverageJSON, List<Beverage> BeverageList) {
        String reason = null;
        if (beverageJSON != null) {
            try {
                JSONArray arr = new JSONArray(beverageJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Beverage beverageDetail = new Beverage(obj.getString(Beverage.EMAIL),
                            obj.getInt(Beverage.ID)
                            , obj.getString(Beverage.IMAGE), obj.getString(Beverage.BDES),
                            obj.getInt(Beverage.RATE), obj.getString(Beverage.BRAND), obj.getString(Beverage.TITLE),
                            obj.getString(Beverage.LOCATION), obj.getInt(Beverage.BYEAR),
                            obj.getInt(Beverage.PERCENTAGE), obj.getString(Beverage.BTYPE),
                            obj.getString(Beverage.STYLE));
                    if (obj.getString(Beverage.DESCRIPTION) != null) {
                        beverageDetail.setmDescription(obj.getString(Beverage.DESCRIPTION));
                    }
                    if (!obj.getString(Beverage.IMAGEADD).equals("null")) {
                        beverageDetail.setmImageAdd(obj.getString(Beverage.IMAGEADD));
                    }

                    BeverageList.add(beverageDetail);
                    Log.i("Doubts", "Beverage owner: " + beverageDetail.getMemail() +
                            "Beverage id: " + beverageDetail.getMid() +
                            beverageDetail.getmImageAdd() +
                            beverageDetail.getmDescription() +
                            beverageDetail.getRate());
                }
            } catch (JSONException e) {
                reason = "Unable to parse data, Reason: " + e.getMessage();
                Log.i("parse data", e.getMessage());
            }

        }
        return reason;
    }


}
