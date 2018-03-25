package com.example.limaoi.gameone.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.limaoi.gameone.R;

/**
 * Created by limaoi on 2017/7/3.
 * E-mail：autismlm20@vip.qq.com
 */

public class KPLfragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpl, container, false);
        return view;
    }
}
