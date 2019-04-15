package com.example.softeng.recipick.AsyncTasks;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.example.softeng.recipick.Activities.AddRecipeActivity;
import com.example.softeng.recipick.Models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UploadRecipeTask extends AsyncTask<Uri[], Integer, Boolean> {

    /** uid of the currently logged in user */
    private String uid;
    /** gets a reference of the firebase storage */
    private StorageReference mStorageRef;

    private CollectionReference userRef;
    private CollectionReference recipeRef;

    private FirebaseAuth mAuth;



    /**
     * Constant used when pushing values into the database+storage
     */
    private static final String USERS = "Users";
    private static final String IMAGES = "Images";
    private static final String RECIPES = "Recipes";

    /** The value that will be returnd */
    private boolean result;

    /** dialog will be displayed till the task is added */
    private ProgressDialog dialog = null;

    /** Counter that increments after every image that has been added to the storage */
    private int counter;

    /** an instance of an activity */
    private AddRecipeActivity mActivity;
    /** The list of images to be uploaded onto firebase */
    private List<Uri> imageList;
    /** The item to be pushed into the database */
    private Recipe mRecipe;

    /** The downloaded list of images */
    List<String> downloadedList;

    /**
     *  Parameterised consturctor
     * @param list
     *          the list of images to be added to the database
     * @param recipe
     *           the item to be posted into firebase
     * @param activity
     *           so that we can interact with the UI
     */
    public UploadRecipeTask(List<Uri> list, Recipe recipe, AddRecipeActivity activity) {
        this.imageList = list;
        this.mRecipe = recipe;
        this.mActivity = activity;
        downloadedList = new ArrayList<>();
    }

    /**
     * Initialising the fields before the do in background method
     */
    @Override
    protected void onPreExecute() {

        mAuth = FirebaseAuth.getInstance();
        /** gets the uid of the currently logged in user */
        uid = mAuth.getCurrentUser().getUid();
        /** gets the reference of firebase storage */
        mStorageRef = FirebaseStorage.getInstance().getReference();
        /** sets the result to true */
        result = true;

        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid).collection(RECIPES);
        recipeRef = FirebaseFirestore.getInstance().collection(RECIPES);

        /** sets the counter */
        counter = 0;
        /** sets the dialog */
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(" Uploading Recipe");
        dialog.show();
    }

    /**
     *
     * result is set to false if anything goes wrong
     * @param files to be downloaded
     * @return
     */
    @Override
    protected Boolean doInBackground(Uri[]... files) {
        /** Goes through each item in the list and uploads the image to the storage */
        for (Uri uri: this.imageList) {
            uploadImage(uri);
        }
        /** Returns the result */
        return result;
    }

    /**
     *    * result is set to false if anything goes wrong
     * @param uri
     *          uploads the URI image into the storage of firebase
     *
     */
    public void uploadImage(Uri uri) {
        /** gets the reference to a path in the storage */
        final StorageReference storageReference = mStorageRef
                .child(uid)
                .child(uri.getLastPathSegment());

        /** Uploads the file to the storage of firebase*/
        storageReference.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        /** After the image has been fully uploaded */
                        /** Increment the counter */
                        if (task.isSuccessful()) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadedList.add(uri.toString());
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    counter++;
                                    /** Set the dialog message according to counter */
                                    dialog.setMessage(" Uploading Images [" + counter + "/" + imageList.size() + "]");
                                    dialog.show();
                                    /** If the counter is equal to the imageList.size(), that means that all of the images have been downloaded
                                     * so now we need to update the data to the database
                                     */
                                    if (counter == imageList.size()) {
                                        uploadData(downloadedList);
                                        mActivity.finish();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            result = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result = false;
                    }
                });
    }

    /**
     *
     * result is set to false if anything goes wrong
     *
     * @param uploadedImages
     *                  gets the downloaded list
     *
     *
     */
    public void  uploadData(List<String> uploadedImages) {
        if (uploadedImages.size()==0) {
            result = false;
        } else {
            /** sets the list in the item object */
            mRecipe.setImages(uploadedImages);

            /** now uploads the value to the firebase database
            pushed.setValue(mRecipe, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError err, DatabaseReference ref) {
                    if (err == null) {
                        if (mRecipe.isShare()) {
                            pushPost.setValue(mRecipe, new DatabaseReference.CompletionListener() {
                                public void onComplete(DatabaseError err, DatabaseReference ref) {
                                    if (err != null) {
                                        result = false;
                                    }
                                }
                            });
                        }
                    } else {
                        result = false;
                    }
                }
            }); */

            recipeRef.add(mRecipe).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    result = false;
                }
            });
        }
    }

    protected void onProgressUpdate(Integer...a){
        super.onProgressUpdate(a);

    }

    /**
     * @param result
     *          prompts the user to try again if false
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            Toasty.success(mActivity, "New Recipe Added!", Toast.LENGTH_LONG, true).show();
        } else {
            Toasty.error(mActivity, "Failed to upload recipe, please try again!", Toast.LENGTH_LONG, true).show();
        }
    }

}
