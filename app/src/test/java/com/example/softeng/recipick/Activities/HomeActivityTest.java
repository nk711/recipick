package com.example.softeng.recipick.Activities;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelStore;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.FragmentController;
import android.support.v4.app.SupportActivity;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class HomeActivityTest {
    @Mock
    HomeActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    @Mock
    ViewPager mViewPager;
    @Mock
    Toolbar toolbar;
    @Mock
    Drawer result;
    @Mock
    FirebaseAuth mAuth;
    @Mock
    AppCompatDelegate mDelegate;
    @Mock
    Resources mResources;
    @Mock
    Handler mHandler;
    @Mock
    FragmentController mFragments;
    @Mock
    ViewModelStore mViewModelStore;
    @Mock
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    @Mock
    SimpleArrayMap<Class<? extends SupportActivity.ExtraData>, SupportActivity.ExtraData> mExtraDataMap;
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    @InjectMocks
    HomeActivity homeActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testNavbar_setup() {
        homeActivity.navbar_setup();
    }

    @Test
    void testOnCreate() {
        homeActivity.onCreate(null);
    }

    @Test
    void testOnCreateOptionsMenu() {
        boolean result = homeActivity.onCreateOptionsMenu(null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testOnOptionsItemSelected() {
        boolean result = homeActivity.onOptionsItemSelected(null);
        Assertions.assertEquals(true, result);
    }
}