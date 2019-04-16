package com.example.softeng.recipick.Activities;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelStore;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentController;
import android.support.v4.app.SupportActivity;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.softeng.recipick.Adapters.ImageListAdapter;
import com.example.softeng.recipick.Adapters.IngredientsAndMeasurementsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

public class AddRecipeActivityTest {
    @Mock
    Uri imageUri;
    @Mock
    Button btnUpload;
    @Mock
    Button btnCamera;
    @Mock
    Button btnSubmit;
    @Mock
    RecyclerView mImages;
    @Mock
    List<String> fileNameList;
    @Mock
    List<Uri> fileList;
    @Mock
    List<String> ingredients;
    @Mock
    List<String> measurements;
    @Mock
    List<String> quantity;
    @Mock
    RecyclerView listView;
    @Mock
    Button btnAddIngredient;
    @Mock
    IngredientsAndMeasurementsAdapter ingredientsAdapter;
    @Mock
    ImageListAdapter adapter;
    @Mock
    AlertDialog.Builder mBuilder;
    @Mock
    View mView;
    @Mock
    EditText txtRecipeName;
    @Mock
    Switch share;
    @Mock
    EditText txtDescription;
    @Mock
    EditText txtPreperation;
    @Mock
    EditText txtDuration;
    @Mock
    EditText txtServings;
    @Mock
    EditText txtBudget;
    @Mock
    EditText txtCalories;
    @Mock
    EditText txtCuisine;
    @Mock
    EditText txtMeals;
    @Mock
    EditText txtIngredient;
    @Mock
    EditText txtMeasurement;
    @Mock
    EditText txtQuantity;
    @Mock
    FirebaseAuth mAuth;
    @Mock
    DocumentReference userRef;
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
    AddRecipeActivity addRecipeActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreate() throws Exception {
        addRecipeActivity.onCreate(null);
    }

    @Test
    public void testLoadUser() throws Exception {
        addRecipeActivity.loadUser();
    }

    @Test
    public void testOnActivityResult() throws Exception {
        addRecipeActivity.onActivityResult(0, 0, null);
    }

    @Test
    public void testGetFileName() throws Exception {
        String result = addRecipeActivity.getFileName(null);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}