package com.zmj.weibomonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ZMJ on 2018/4/28.
 */

public class DisplayActivity extends BaseActivity {
    @Override
    protected Fragment getFragment() {
        return new DisplayFragment();
    }

}
