package yyang3.tacoma.uw.edu.craftcellar;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by RickYang on 6/1/2016.
 */
public class CellarActivityTest extends ActivityInstrumentationTestCase2<CellarActivity> {
    private Solo solo;
    public  CellarActivityTest () {
        super("com.example.app", CellarActivity.class);
    }
    public CellarActivityTest(Class<CellarActivity> activityClass) {

        super(activityClass);
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }
    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testLogin() {
        boolean fragmentLoaded = solo.searchText("Sign in");
        assertTrue("Course List fragment loaded", fragmentLoaded);
        boolean textFound = solo.searchText("Email");
        assertTrue("Login fragment loaded", textFound);
        solo.enterText(0, "yyang3@uw.edu");
        solo.enterText(1, "111111");
        solo.clickOnButton("Sign in");
        boolean worked = solo.searchText("Welcome");
        assertTrue("Sign in worked!", worked);
    }


}
