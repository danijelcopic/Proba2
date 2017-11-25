package rs.aleph.android.example13;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import rs.aleph.android.example13.activities.activity.SecondActivity;

public class SecondActivityTest extends ActivityInstrumentationTestCase2<SecondActivity> {

    SecondActivity activity;

    public SecondActivityTest(){
        super(SecondActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testMainActivityView(){

    }
}
