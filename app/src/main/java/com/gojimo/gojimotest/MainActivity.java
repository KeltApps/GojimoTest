    package com.gojimo.gojimotest;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gojimo.gojimotest.fragments.QualificationsFragment;

    public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQualificationsFragment();
    }

    private void startQualificationsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QualificationsFragment qualificationsFragment = new QualificationsFragment();
        fragmentTransaction.replace(R.id.activity_main_container, qualificationsFragment, getString(R.string.fragment_qualifications));
        fragmentTransaction.commitAllowingStateLoss();
    }

        @Override
        public boolean onSupportNavigateUp() {
            //This method is called when the up button is pressed. Just the pop back stack.
            getSupportFragmentManager().popBackStack();
            return true;
        }

}
