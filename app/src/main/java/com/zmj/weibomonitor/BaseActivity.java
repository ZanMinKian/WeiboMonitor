package com.zmj.weibomonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ZMJ on 2018/4/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract Fragment getFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=getFragment();
        fm.beginTransaction().add(R.id.container,fragment).commit();

    }
}
