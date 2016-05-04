package yyang3.tacoma.uw.edu.craftcellar.Beverage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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

    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String IMAGE = "imageadd";
    public static final String DESCRIPTION = "description";
    public static final String RATE = "rate";

    public Beverage(String memail, int mid, String mImageAdd, String mDescription, int rate) {
        this.memail = memail;
        this.mid = mid;
        this.mImageAdd = mImageAdd;
        this.mDescription = mDescription;
        this.rate = rate;
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
                            , obj.getString(Beverage.IMAGE), obj.getString(Beverage.DESCRIPTION), obj.getInt(Beverage.RATE));
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
