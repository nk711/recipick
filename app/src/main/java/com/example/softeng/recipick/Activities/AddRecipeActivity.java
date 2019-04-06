package com.example.softeng.recipick.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softeng.recipick.Adapters.ImageListAdapter;
import com.example.softeng.recipick.Adapters.IngredientsAndMeasurementsAdapter;
import com.example.softeng.recipick.Models.Ingredient;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.R;
import com.example.softeng.recipick.AsyncTasks.UploadRecipeTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddRecipeActivity extends AppCompatActivity {
    /** The request code in order for the user to select multiple images */
    private static final int RESULT_LOAD_IMAGE = 1;



    private Uri imageUri;

    private static final int REQUEST_CAPTURE_IMAGE = 100;


    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
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
    /** the context of the passed activity*/

    private RecyclerView listView;

    private Button btnAddIngredient;

    private IngredientsAndMeasurementsAdapter ingredientsAdapter;

    /** custom adapter to set the recycler view's state */
    private ImageListAdapter adapter;
    /** Allows us to create custom dialogs */
    private AlertDialog.Builder mBuilder;
    /** Allows us to create custom dialogs */
    private View mView;
    /** input fields for the user to fill in */
    private TextView txtRecipeName;
    private Switch share;
    private TextView txtDescription;
    private TextView txtPreperation;
    private TextView txtDuration;
    private TextView txtServings;
    private TextView txtBudget;
    private TextView txtCalories;
    private TextView txtCuisine;

    private TextView txtIngredient;
    private TextView txtMeasurement;
    private TextView txtQuantity;

    private String author;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference getUserDetails;
    private String uid;


    private static final String USERS = "Users";
    private static final String CONTACTS = "Contacts";
    private static final String IMAGES = "Images";
    private static final String RECIPES = "Recipes";

    /** The object created when the post button has been clicked*/
    private Recipe mRecipe;
    /** The currently logged in user's details */
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        /** sets up the back icon on the action bar and allows the user to go back */

        /** Initialising components from layout */
        btnUpload = findViewById(R.id.btnUpload);
        btnCamera = findViewById(R.id.btnCamera);
        btnSubmit = findViewById(R.id.btnSubmit);

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


        ingredients = new ArrayList<>();
        measurements = new ArrayList<>();
        quantity = new ArrayList<>();


        ingredientsAdapter = new IngredientsAndMeasurementsAdapter(ingredients, measurements, quantity);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(ingredientsAdapter);


        fileNameList = new ArrayList<>();
        fileList = new ArrayList<>();
        adapter = new ImageListAdapter(fileNameList, fileList);
        mImages.setLayoutManager(new LinearLayoutManager(this));
        mImages.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        getUserDetails = mDatabase.getReference().child(USERS);

        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();


        getUserDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    author = user.getDisplay_name();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /**
         * When the user presses the button to upload images...
         */
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                }

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
        });

        /**
         * When the user presses the button to upload images...
         */
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Creates a new intent
                 * Can only upload files that are of type images.
                 * User can multi-select images
                 */
                openCameraIntent();
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.add(txtIngredient.getText().toString());
                measurements.add(txtMeasurement.getText().toString());
                quantity.add(txtQuantity.getText().toString());
                ingredientsAdapter.notifyDataSetChanged();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
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
         *
         */
        if ( this.fileNameList.size()<=6) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                /** If the user selected multiple images... */
                if (data.getClipData() != null) {
                    /** Get the total selected images    */
                    int totalSelected = data.getClipData().getItemCount();
                    /** gets the count of the existing selected images + the new selected images*/
                    size = size+totalSelected;
                    /** Checks if its greater than 4 => cannot be greater than 4*/
                    if (size<=6) {
                        /** If everything is valid... Go through each file*/
                        for (int i = 0; i < totalSelected; i++) {
                            /** get the file directory of the current image*/
                            Uri file = data.getClipData().getItemAt(i).getUri();
                            /** gets the file name of the current image */
                            String fileName = getFileName(file);
                            /** Adds the file to the recycler view and updates its state */
                            fileNameList.add(fileName);
                            fileList.add(file);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        /** Display message if size > 6 */
                        Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
                    }
                    /** If the user selects only one image     */
                } else if (data.getData() != null) {
                    /** gets the count of the existing selected images + the new selected images*/
                    size = size +1;
                    /** Checks if its greater than 4 => cannot be greater than 6*/
                    if (size<=6) {
                        /** get the file directory of the current image*/
                        Uri file = data.getData();
                        /** gets the file name of the current image */
                        String fileName = getFileName(file);
                        /** Adds the file to the recycler view and updates its state */
                        fileNameList.add(fileName);
                        fileList.add(file);
                        adapter.notifyDataSetChanged();
                    } else {
                        /** Display message if size > 4 */
                        Toast.makeText(this, "You can only upload 6 Images!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                size = size+1;
                if (size<=6){

                    /** get the file directory of the current image*/
                    Uri file = imageUri;
                    /** gets the file name of the current image */
                    String fileName = getFileName(file);
                    /** Adds the file to the recycler view and updates its state */
                    fileNameList.add(fileName);
                    fileList.add(file);
                    adapter.notifyDataSetChanged();
                } else {
                    /** Display message if size > 6 */
                    Toast.makeText(this, "You can only upload 4 Images!", Toast.LENGTH_SHORT).show();
                }

            }

        }  else {
            /** Display message if size > 6 */
            Toast.makeText(this, "You can only upload 4 Images!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *
     * Code taken from:
     * https://www.youtube.com/watch?v=CXR8-9amqGo
     * 'TVAC Studio'
     *
     * @param uri
     *          the uri of the selected image passed into the method to retrieve the file name
     *
     * @returns the file names fo the images the users have selected [VERY HELPFUL]
     */
    public String getFileName(Uri uri) {
        String filename = null;
        /** Determines the name of the uri through the use of cursors */
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

    private void openCameraIntent() {
        if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
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

    private void createRecipe() {


        if (validation()) {
            List<Ingredient> listOfIngredients = new ArrayList<>();
            for (int i = 0; i < ingredients.size(); i++) {
                listOfIngredients.add(new Ingredient(ingredients.get(i), quantity.get(i), measurements.get(i)));
            }
            Recipe recipe = new Recipe (
                    txtRecipeName.getText().toString(),
                    txtDescription.getText().toString(),
                    listOfIngredients,
                    txtPreperation.getText().toString(),
                    Integer.parseInt(txtDuration.getText().toString()),
                    Integer.parseInt(txtCalories.getText().toString()),
                    Double.parseDouble(txtBudget.getText().toString()),
                    Integer.parseInt(txtServings.getText().toString()),
                    txtCuisine.getText().toString(),
                    share.isChecked(),
                    author
            );

            UploadRecipeTask upload = new UploadRecipeTask(fileList, recipe, AddRecipeActivity.this);
            upload.execute();


        }

    }

    private boolean validation() {

        /** Need to validate
         *  -Duration
         *  -Calories
         *  -Budget
         *  -Servings
         */

        boolean result = true;

        /** Checks if fields are blank */
        if (txtRecipeName.getText().toString().isEmpty()||
                txtDescription.getText().toString().isEmpty()||
                txtPreperation.getText().toString().isEmpty()||
                txtDuration.getText().toString().isEmpty()||
                txtCalories.getText().toString().isEmpty()||
                txtBudget.getText().toString().isEmpty()||
                txtServings.getText().toString().isEmpty()||
                txtCuisine.getText().toString().isEmpty()||
                fileList.isEmpty() ||
                fileNameList.isEmpty() ||
                ingredients.isEmpty() ||
                measurements.isEmpty() ||
                quantity.isEmpty()) {
            result = false;
        }




        return result;
    }

    /** Associated with Enum classes */
    private void chooseCuisineDialog() {

    }

    private void chooseMeasurementDialog() {

    }



}
