package yyang3.tacoma.uw.edu.craftcellar;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;

/**
 * Created by RickYang on 6/1/2016.
 */
public class BeverageTest extends TestCase {

    private Beverage mBeverage;
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

    @Test
    public void testConstructor () {
        Beverage b = new Beverage("yyang3", 10, "test", "test", 10,
        "fsd", "sdfa", "fdas", 3, 3, "dfadsa", "fadsa");
        assertNotNull(b);
    }

    @Before
    public void setUp() {
        memail = "yyang3";
        mid = 10;
        mImageAdd = "test";
        mDescription = "testd";
        rate = 8;
        mBrand = "fsd";
        mTitle = "sdfa";
        mLocation = "fdas";
        mByear = 3;
        mPercentage = 4;
        myBtype = "dfadsa";
        mStyle ="fadsa";

        mBeverage = new Beverage(memail, mid, mImageAdd, mDescription, rate,
                mBrand, mTitle, mLocation, mByear, mPercentage, myBtype, mStyle);
    }
    @Test
    public void testgetStyle () {
        assertEquals(mStyle,mBeverage.getmStyle());
    }
    @Test
    public void testgetType () {
        assertEquals(myBtype,mBeverage.getMyBtype());
    }
    @Test
    public void testgetpercentage () {
        assertEquals(mPercentage,mBeverage.getmPercentage());
    }
    @Test
    public void testgetyr () {
        assertEquals(mByear,mBeverage.getmByear());
    }
    @Test
    public void testgetLocation () {
        assertEquals(mLocation,mBeverage.getmLocation());
    }
    @Test
    public void testgetTitle () {
        assertEquals(mTitle,mBeverage.getmTitle());
    }
    @Test
    public void testgetBrand () {
        assertEquals(mBrand,mBeverage.getmBrand());
    }
    @Test
    public void testgetrate () {
        assertEquals(rate,mBeverage.getRate());
    }
    @Test
    public void testgetDesc () {
        assertEquals(mDescription,mBeverage.getmDescription());
    }
    @Test
    public void testgetImage () {
        assertEquals(mImageAdd,mBeverage.getmImageAdd());
    }
    @Test
    public void testgetEmail () {
        assertEquals(memail,mBeverage.getMemail());
    }
    @Test
    public void testgetID () {
        assertEquals(mid,mBeverage.getMid());
    }

    @Test
    public void testSetNegativeId() {
        try {
            mBeverage.setMid(-1);
            fail("Beverage Id can be set to negative");
        }catch (IllegalArgumentException e) {

        }
    }
    @Test
    public void testSetFutureByear() {
        try {
            mBeverage.setmByear(3000);
            fail("Brew year can be future");
        }catch (IllegalArgumentException e) {

        }
    }
    @Test
    public void testSetNegPercentage() {
        try {
            mBeverage.setmPercentage(-1);
            fail("alcohol percentage can be negative");
        }catch (IllegalArgumentException e) {

        }
    }
    @Test
    public void testSetNegRate() {
        try {
            mBeverage.setRate(-1);
            fail("rate can be negative");
        }catch (IllegalArgumentException e) {

        }
    }
    @Test
    public void testparseJSON () {
       String temp = "[{\"email\":\"yyang3@uw.edu\"," +
               "\"imageadd\":null,\"description\":\"FFDJ;ALKSKLFJA;LJFDLAJDLFKSJA\"," +
               "\"rate\":\"4\",\"id\":\"4\"}]";
        String message =  Beverage.parseBeveragesJSON(temp
                , new ArrayList<Beverage>());
        assertTrue("JSON With Valid String", message == null);
    }
}
