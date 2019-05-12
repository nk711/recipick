/**
 * AddRecipeActivity.java
 */
package com.example.softeng.recipick.Activities;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import com.example.softeng.recipick.Adapters.ImageListAdapter;
import com.example.softeng.recipick.Adapters.IngredientsAndMeasurementsAdapter;
import com.example.softeng.recipick.Models.Ingredient;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.example.softeng.recipick.AsyncTasks.UploadRecipeTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Task that allows the user to add a new recipe
 */
public class AddRecipeActivity extends AppCompatActivity {
    private static final String TAG = "AddRecipeActivity";
    /** The request code in order for the user to select multiple images */
    private static final int RESULT_LOAD_IMAGE = 1;
    /** Holds the uri of an image*/
    private Uri imageUri;
    /** The request code in order for the user to capture an image */
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    /** The request code to allow the user to use the camera */
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    /** The request code to allow the user to use the device's external devices */
    private final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;
    /** The title of the activity */
    private static final String TITLE = "Add a Recipe";
    /** button that will allow the user to select images to upload */
    private Button btnUpload;
    /** button that will allow the user to take a picture to be uploaded */
    private Button btnCamera;
    /** button that will upload the recipe to the database */
    private Button btnSubmit;
    /** Recycler view will hold the list of selected images */
    private RecyclerView mImages;
    /** Holds the list of file names     */
    private List<String> fileNameList;
    /** Holds the list of file directories as URI */
    private List<Uri> fileList;
    /** the list of ingredient's name   */
    public List<String> ingredients;
    /** the list of ingredient's measurements */
    public List<String> measurements;
    /** the list of ingredient's quantity */
    public List<String> quantity;
    /** holds the list of ingredients for the user*/
    private RecyclerView listView;
    /** button component that allows the user to add the ingredients */
    private Button btnAddIngredient;
    /** Image button component, when clicked will close the current activity and go back to the main page */
    private ImageButton btnBackToMain;
    /** Adapter used to populate and view the user added ingredients */
    private IngredientsAndMeasurementsAdapter ingredientsAdapter;
    /** custom adapter to set the recycler view's state */
    private ImageListAdapter adapter;
    /** Allows us to create custom dialogs */
    private AlertDialog.Builder mBuilder;
    /** Allows us to create custom dialogs */
    private View mView;
    /** input fields for the user to fill in */
    private EditText txtRecipeName;
    /** decides whether the post should be shared or viewed locally */
    private Switch share;
    /** recipe's description */
    private EditText txtDescription;
    /** recipe's preperation */
    private EditText txtPreperation;
    /** recipe's cooking duration */
    private EditText txtDuration;
    /** The number of servings a recipe can make*/
    private EditText txtServings;
    /** recipe's budget */
    private EditText txtBudget;
    /** recipe's calories */
    private EditText txtCalories;
    /** recipe's cuisine type */
    private EditText txtCuisine;
    /** recipe's meal type, ie Lunch, Dinner, Snack*/
    private EditText txtMeals;
    /** used to add recipe's ingredients */
    private EditText txtIngredient;
    /** used to add an ingredient's measurements for a recipe */
    private EditText txtMeasurement;
    /** used to add an ingredient's quantity for a recipe */
    private EditText txtQuantity;
    /** the author of the recipe being created*/
    private String author;
    /** used to get the user */
    private FirebaseAuth mAuth;
    /** holds the user id of the currently logged in user*/
    private String uid;
    /** holds a document reference path to the user section in firestorage */
    private DocumentReference userRef;
    /** Only letters and spaces allowed */
    private static final String REGEX = "[A-z ]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        /** sets up the back icon on the action bar and allows the user to go back */

        /** Initialising components from layout */
        btnUpload = findViewById(R.id.btnUpload);
        btnCamera = findViewById(R.id.btnCamera);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBackToMain = findViewById(R.id.backToMain);
        mImages =  findViewById(R.id.list_images);
        listView = findViewById(R.id.list_of_ingredients);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        txtIngredient= findViewById(R.id.txtIngredient);
        txtMeasurement= findViewById(R.id.txtMeasurement);
        txtQuantity= findViewById(R.id.txtQuantity);
        txtRecipeName = findViewById(R.id.txtRecipeName);
        share = findViewById(R.id.sShare);
        txtDescription= findViewById(R.id.txtDescription);
        txtPreperation= findViewById(R.id.txtPreperation);
        txtDuration= findViewById(R.id.txtDuration);
        txtServings= findViewById(R.id.txtServings);
        txtBudget= findViewById(R.id.txtBudget);
        txtCalories= findViewById(R.id.txtCalories);
        txtCuisine= findViewById(R.id.txtCuisine);
        txtMeals = findViewById(R.id.txtMeals);

        //initialising ingredient lists
        ingredients = new ArrayList<>();
        measurements = new ArrayList<>();
        quantity = new ArrayList<>();


        //Setting up ingredients adapter
        ingredientsAdapter = new IngredientsAndMeasurementsAdapter(ingredients, measurements, quantity, TAG);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(ingredientsAdapter);

        //Setting up file list adapter
        fileNameList = new ArrayList<>();
        fileList = new ArrayList<>();
        adapter = new ImageListAdapter(fileNameList, fileList);
        mImages.setLayoutManager(new LinearLayoutManager(this));
        mImages.setAdapter(adapter);

        //Gets the currently logged in user
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        // reference to the user collection in firestorage
        userRef = FirebaseFirestore.getInstance().collection(Utility.USERS).document(uid);

        //updates user's information in shared preference
        loadUser();

        /**
         * When the user presses the button to upload images...
         */
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                } else {
                    /**
                     * Creates a new intent
                     * Can only upload files that are of type images.
                     * User can multi-select images
                     */
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);
                }
            }
        });

        /**
         * When the user presses the button to upload images...
         */
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    /**
                     * Creates a new intent
                     * Can only upload files that are of type images.
                     * User can multi-select images
                     */
                    openCameraIntent();
                }
            }
        });

        /**
         * Allows the user to add ingredients to a list
         */
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtIngredient.getText().toString().trim().equals("") &&
                        txtIngredient.getText().toString().trim().matches(REGEX) &&
                        !txtMeasurement.getText().toString().trim().equals("") &&
                        txtMeasurement.getText().toString().trim().matches(REGEX) &&
                        !txtQuantity.getText().toString().trim().equals("")) {

                    ingredients.add(txtIngredient.getText().toString().trim());
                    measurements.add(txtMeasurement.getText().toString().trim());
                    quantity.add(txtQuantity.getText().toString().trim());
                    ingredientsAdapter.notifyDataSetChanged();

                    txtIngredient.setText("");
                    txtMeasurement.setText("");
                    txtQuantity.setText("");
                } else {
                    Toasty.warning(AddRecipeActivity.this, "Enter a valid ingredient, measurement and quantity", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        /** once pressed will close the activity and will load the main page*/
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /** Creates the recipe */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectedToInternet()) {
                    createRecipe();
                } else {
                    Toasty.warning(AddRecipeActivity.this, "Please check your internet connection.", Toasty.LENGTH_LONG, true).show();
                }
            }
        });


    }

    /**
     *  Used to get the display name of the currently logged in user
     */
    public void loadUser() {
            userRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                author = user.getDisplay_name();
                            } else {
                                //error user doesnt extist
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                                //error message
                                //failed to load user details
                        }
                    });
    }

    /**
     * When the user selects the images The following happens.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** Checks the size of the file name list*/
        int size =  this.fileNameList.size();
        /**
         * Checks if the file name list is greater than 6
         * as the user cannot upload more than 6 images
         */
        if ( this.fileNameList.size()<=6) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                // If the user selected multiple images...
                if (data.getClipData() != null) {
                    // Get the total selected images
                    int totalSelected = data.getClipData().getItemCount();
                    // gets the count of the existing selected images + the new selected images
                    size = size+totalSelected;
                    // Checks if its greater than 6 => cannot be greater than 6
                    if (size<=6) {
                        // If everything is valid... Go through each file
                        for (int i = 0; i < totalSelected; i++) {
                            // get the file directory of the current image
                            Uri file = data.getClipData().getItemAt(i).getUri();
                            // gets the file name of the current image
                            String fileName = getFileName(file);
                            // Adds the file to the recycler view and updates its state
                            fileNameList.add(fileName);
                            fileList.add(file);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // Display message if size > 6
                        Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
                    }
                    // If the user selects only one image
                } else if (data.getData() != null) {
                    // gets the count of the existing selected images + the new selected images
                    size = size +1;
                    //Checks if its greater than 4 => cannot be greater than 6
                    if (size<=6) {
                        // get the file directory of the current image
                        Uri file = data.getData();
                        // gets the file name of the current image
                        String fileName = getFileName(file);
                        // Adds the file to the recycler view and updates its state
                        fileNameList.add(fileName);
                        fileList.add(file);
                        adapter.notifyDataSetChanged();
                    } else {
                        // Display message if size > 4
                        Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                size = size+1;
                if (size<=6){
                    // get the file directory of the current image
                    Uri file = imageUri;
                    // gets the file name of the current image
                    String fileName = getFileName(file);
                    // Adds the file to the recycler view and updates its state
                    fileNameList.add(fileName);
                    fileList.add(file);
                    adapter.notifyDataSetChanged();
                } else {
                    // Display message if size > 6
                    Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
                }
            }
        }  else {
            // Display message if size > 6
            Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *
     * Reference: https://www.youtube.com/watch?v=CXR8-9amqGo
     * 'TVAC Studio'
     *
     * @param uri
     *          the uri of the selected image passed into the method to retrieve the file name
     *
     * @returns the file names fo the images the users have selected [VERY HELPFUL]
     */
    public String getFileName(Uri uri) {
        String filename = null;
        // Determines the name of the uri through the use of cursors
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (filename==null) {
            filename = uri.getPath();
            int cut = filename.lastIndexOf('/');
            if (cut != -1) {
                filename = filename.substring(cut + 1);
            }
        }
        return filename;
    }


    /**
     *
     * @return the taken image
     *
     * @throws IOException
     *          Throws IO exception if any errors occur
     */
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageUri = Uri.fromFile(image);
        return image;
    }

    /**
     *  Using a camera intent in order to capture an image and save it
     */
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,  "com.example.softeng.recipick.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    /**
     *  Validates all fields before adding the recipe to firestorage
     */
    private void createRecipe() {
        //Validates the fields before saving the files
        if (validation()) {
            List<Ingredient> listOfIngredients = new ArrayList<>();
            Map<String, Boolean> ingredientsQuery = new HashMap<>();
            //populates the ingredients lists
            for (int i = 0; i < ingredients.size(); i++) {
                listOfIngredients.add(new Ingredient(ingredients.get(i).toLowerCase().trim(),
                                                     quantity.get(i).toLowerCase().trim(),
                                                     measurements.get(i).toLowerCase().trim()));
                ingredientsQuery.put(ingredients.get(i).toLowerCase().trim(), true);
            }
            //Creates a recipe object
            Recipe recipe = new Recipe (
                    uid,
                    txtRecipeName.getText().toString().trim(),
                    txtDescription.getText().toString().trim(),
                    listOfIngredients,
                    ingredientsQuery,
                    txtPreperation.getText().toString().trim(),
                    Integer.parseInt(txtDuration.getText().toString().trim()),
                    Integer.parseInt(txtCalories.getText().toString().trim()),
                    Double.parseDouble(txtBudget.getText().toString().trim()),
                    Integer.parseInt(txtServings.getText().toString().trim()),
                    txtCuisine.getText().toString().trim(),
                    txtMeals.getText().toString().trim(),
                    share.isChecked(),
                    author
            );
            //Uses an async task to asynchronously save the images and store the recipe details on fire storage
            UploadRecipeTask upload = new UploadRecipeTask(fileList, recipe, AddRecipeActivity.this);
            upload.execute();
        } else {
            Toasty.warning(AddRecipeActivity.this, "Make sure all fields are filled", Toasty.LENGTH_SHORT, true).show();
        }
    }

    /**
     * @return true if the validation passes
     *          false if the validation fails
     */
    private boolean validation() {
        boolean result = true;
        // Checks if fields are blank
        if (txtRecipeName.getText().toString().trim().isEmpty()||
                txtDescription.getText().toString().trim().isEmpty()||
                txtPreperation.getText().toString().trim().isEmpty()||
                txtDuration.getText().toString().trim().isEmpty()||
                txtCalories.getText().toString().trim().isEmpty()||
                txtBudget.getText().toString().trim().isEmpty()||
                txtServings.getText().toString().trim().isEmpty()||
                txtCuisine.getText().toString().trim().isEmpty()||
                fileList.isEmpty() ||
                fileNameList.isEmpty() ||
                ingredients.isEmpty() ||
                measurements.isEmpty() ||
                quantity.isEmpty()) {
            result = false;
        } else if (!txtRecipeName.getText().toString().trim().matches(REGEX)) {
            result = false;
        } else if (!txtCuisine.getText().toString().trim().matches(REGEX)) {
            result = false;
        } else if (!txtMeals.getText().toString().trim().matches(REGEX)) {
            result = false;
        }
        return result;
    }

    /**
     * A method that checks if there is an internet connection.
     *
     * @return whether or not there is an internet connection.
     */
    public boolean connectedToInternet() {
        // Creates a ConnectivityManager object that checks the connectivity service
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // If there is a connectivity service then get its information
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            // If network info is not null, check if the state of the network info is connected
            if (networkInfos != null) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * When the activity enters the onsaveinstance the arraylist that is used to populate the recycler view is saved
     * onsaveinstance retains the recycler view upon rotation of the device
     * @param outState
     *      Will hold the list of filenames and the list of file directory
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("filenames", new ArrayList<>(fileNameList));
        outState.putParcelableArrayList("files", new ArrayList<>(fileList));
    }

    /**
     * Restores the data and reset's the recycler view
     * retains the recycler view upon rotation of the device
     * @param savedInstanceState
     *          returns the saved list of filenames and file directory
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileNameList=  savedInstanceState.getStringArrayList("filenames");
        fileList =  savedInstanceState.getParcelableArrayList("files");
        adapter = new ImageListAdapter(fileNameList, fileList);
        mImages.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
        mImages.setAdapter(adapter);
    }


    /** TODO: Add custom dialogs for user to pick values from enum class
    private void chooseCuisineDialog() {
    } */
    /** TODO: Add custom dialogs for user to pick measurements from enum class
    private void chooseMeasurementDialog() {
    }*/



}
