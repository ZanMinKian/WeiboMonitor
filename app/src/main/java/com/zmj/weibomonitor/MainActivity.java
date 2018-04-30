package com.zmj.weibomonitor;

import android.support.v4.app.Fragment;

public class MainActivity extends BaseActivity {

    @Override
    protected Fragment getFragment() {
        return new ConsoleFragment();
    }
}
