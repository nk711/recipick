/**
 * DiscoverTab.java
 */
package com.example.softeng.recipick.Fragments;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.softeng.recipick.R;

/**
 * Sponsored recipe "packages"
 */
public class DiscoverTab extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discovertab_layout, container, false);
    }
}
