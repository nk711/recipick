package com.example.softeng.recipick.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.softeng.recipick.Activities.AddRecipeActivity;
import com.example.softeng.recipick.Adapters.ImageListAdapter;
import com.example.softeng.recipick.Adapters.SharedImageAdapter;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipePhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipePhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipePhotosFragment extends Fragment {


    private static final String TAG = "Recipe Photos";

    /** The request code in order for the user to select multiple images */
    private static final int RESULT_LOAD_IMAGE = 1;


    /** uid of the currently logged in user */
    private String uid;
    /** gets a reference of the firebase storage */
    private StorageReference mStorageRef;

    private CollectionReference userRef;
    private DocumentReference recipeRef;

    private FirebaseAuth mAuth;


    private Boolean result;


    private Uri imageUri;

    private static final int REQUEST_CAPTURE_IMAGE = 100;


    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /** Holds the selected recipe */
    private Recipe mRecipe;
    private String mRecipe_id;

    private Button btnCamera;
    private Button btnShare;
    private Button btnGallery;

    /** Recycler view will hold the list of selected images */
    private RecyclerView mImages;

    private GridView mSharedImages;

    /** Holds the list of file names     */
    private List<String> fileNameList;
    /** Holds the list of file directories as URI */
    private List<Uri> fileList;

    /** custom adapter to set the recycler view's state */
    private ImageListAdapter adapter;
    private SharedImageAdapter sharedImageAdapter;

    /** The downloaded list of images */
    private List<String> downloadedList;


    private OnFragmentInteractionListener mListener;

    public RecipePhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipePhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipePhotosFragment newInstance(String param1, String param2) {
        RecipePhotosFragment fragment = new RecipePhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recipe_photos, container, false);
        /** gets the bundle from the previous activity */
        Bundle extras = requireActivity().getIntent().getExtras();
        /** checks if the bundle is null */
        if (extras!=null) {
            /** if not set the item */
            mRecipe = (Recipe)extras.getSerializable(Utility.RECIPE);
            mRecipe_id = extras.getString(Utility.ID);

        }

        result = false;
        downloadedList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        /** gets the uid of the currently logged in user */
        uid = mAuth.getCurrentUser().getUid();
        /** gets the reference of firebase storage */
        mStorageRef = FirebaseStorage.getInstance().getReference();


        userRef = FirebaseFirestore.getInstance().collection(Utility.USERS).document(Utility.getUid()).collection(Utility.RECIPE);


        btnShare= view.findViewById(R.id.btnShare);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnCamera = view.findViewById(R.id.btnCamera);
        mImages =  view.findViewById(R.id.list_images);
        mSharedImages = view.findViewById(R.id.list_shared_images);

        fileNameList = new ArrayList<>();
        fileList = new ArrayList<>();
        adapter = new ImageListAdapter(fileNameList, fileList);
        mImages.setLayoutManager(new LinearLayoutManager(requireContext()));
        mImages.setAdapter(adapter);

        sharedImageAdapter = new SharedImageAdapter(requireContext(), mRecipe.getSharedImages());
        mSharedImages.setAdapter(sharedImageAdapter);


        if (savedInstanceState != null) {
            fileNameList=  savedInstanceState.getStringArrayList("filenames");
            fileList =  savedInstanceState.getParcelableArrayList("files");
            downloadedList = savedInstanceState.getStringArrayList("downloaded");
            adapter = new ImageListAdapter(fileNameList, fileList);
            mImages.setLayoutManager(new LinearLayoutManager(requireContext()));
            mImages.setAdapter(adapter);
            if (fileList.size()==0) {
                btnShare.setVisibility(View.GONE);
            }
        }

        recipeRef = FirebaseFirestore.getInstance().collection(Utility.RECIPES).document(mRecipe_id);

        if (fileList.size()==0) {
            btnShare.setVisibility(View.GONE);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Checks permission for camera */
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    /**
                     * Creates a new intent
                     * Can only upload files that are of type images.
                     * User can multi-select images
                     */
                    if (fileList.size() == 0) {
                        openCameraIntent();
                    } else {
                        Toasty.info(requireContext(), "You can only upload one image", Toasty.LENGTH_LONG).show();
                    }
                }

            }
        });


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Checks permission for external storage */
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                } else {
                    if (fileList.size() == 0) {
                        /**
                         * Creates a new intent
                         * Can only upload files that are of type images.
                         * User can multi-select images
                         */
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);
                    } else {
                        Toasty.info(requireContext(), "You can only upload one image", Toasty.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!result) {
                    if (fileList.size() > 1) {
                        Toasty.info(requireContext(), "You can only upload one image", Toasty.LENGTH_LONG).show();
                    } else {
                        if (mRecipe != null && mRecipe_id != null) {
                            result = true;
                            uploadImages();
                        } else {
                            Toasty.error(requireContext(), "Something went wrong, try again later!", Toasty.LENGTH_LONG).show();
                            requireActivity().onBackPressed();
                        }
                    }
                } else {
                      Toasty.info(requireContext(), "Uploading...", Toasty.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    /**
     * When the user selects the images The following happens.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Checks the size of the file name list*/
        int size =  this.fileNameList.size();

        /**
         * Checks if the file name list is greater than 6
         * as the user cannot upload more than 6 images
         *
         */
        if ( this.fileNameList.size()<=1) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                /** If the user selected multiple images... */
                if (data.getClipData() != null) {
                    /** Get the total selected images    */
                    int totalSelected = data.getClipData().getItemCount();
                    /** gets the count of the existing selected images + the new selected images*/
                    size = size+totalSelected;
                    /** Checks if its greater than 1 => cannot be greater than 1*/
                    if (size<=1) {
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
                        /** Display message if size > 1 */
                        Toasty.error(requireContext(), "You can only upload one image!", Toasty.LENGTH_SHORT).show();
                    }
                    /** If the user selects only one image     */
                } else if (data.getData() != null) {
                    /** gets the count of the existing selected images + the new selected images*/
                    size = size +1;
                    /** Checks if its greater than 1 => cannot be greater than 1*/
                    if (size<=1) {
                        /** get the file directory of the current image*/
                        Uri file = data.getData();
                        /** gets the file name of the current image */
                        String fileName = getFileName(file);
                        /** Adds the file to the recycler view and updates its state */
                        fileNameList.add(fileName);
                        fileList.add(file);
                        adapter.notifyDataSetChanged();
                    } else {
                        /** Display message if size > 1 */
                        Toasty.error(requireContext(), "You can only upload one image!", Toasty.LENGTH_SHORT).show();
                    }
                }

            }

            if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                size = size+1;
                if (size<=1){

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
                    Toasty.error(requireContext(), "You can only upload one image!", Toasty.LENGTH_SHORT).show();
                }

            }

        }  else {
            /** Display message if size > 6 */
            Toasty.error(requireContext(), "You can only upload one image!", Toasty.LENGTH_SHORT).show();
        }

        if (fileList.size()==0) {
            btnShare.setVisibility(View.GONE);
        } else {
            btnShare.setVisibility(View.VISIBLE);
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
            Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
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
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageUri = Uri.fromFile(image);
        return image;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(requireActivity().getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),  "com.example.softeng.recipick.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    /** Uploads the image, User can only post one image of their creation time
     *  Uploads the image onto firebase storage, then we take the URL and update the recipe record.
     */
    private void uploadImages() {
        if (fileList.size()!=0) {
            final Uri uri = fileList.get(0);
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
                                storageReference.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadedList.add(uri.toString());
                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                result = false;
                                                if (task.isSuccessful()) {
                                                    downloadedList.add(uri.toString());
                                                    mRecipe.addToSharedImages(downloadedList.get(0));
                                                    recipeRef.update("sharedImages", FieldValue.arrayUnion(downloadedList.get(0)))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toasty.success(requireContext(), "Your photo has been shared", Toasty.LENGTH_LONG).show();
                                                                        downloadedList = new ArrayList<>();
                                                                        fileList = new ArrayList<>();
                                                                        fileNameList = new ArrayList<>();
                                                                        btnShare.setVisibility(View.GONE);
                                                                        adapter = new ImageListAdapter(fileNameList, fileList);
                                                                        mImages.setAdapter(adapter);
                                                                        sharedImageAdapter = new SharedImageAdapter(requireContext(), mRecipe.getSharedImages());
                                                                        mSharedImages.setAdapter(sharedImageAdapter);

                                                                    } else {
                                                                        Toasty.error(requireContext(), "Oops something went wrong!", Toasty.LENGTH_LONG).show();

                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Toasty.error(requireContext(), "Oops something went wrong!", Toasty.LENGTH_LONG).show();

                                                }

                                            }
                                        });
                            } else {
                                Toasty.error(requireContext(), "An error had occurred! Please try again later", Toasty.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            result=false;
            Toasty.info(requireContext(), "Select an image to share.", Toasty.LENGTH_LONG).show();

        }
    }



    /**
     * When the activity enters the onsaveinstance the arraylist that is used to populate the recycler view is saved
     * onsaveinstance retains the recycler view upon rotation of the device
     * @param outState
     *      Will hold the list of filenames and the list of file directory
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("filenames", new ArrayList<String>(fileNameList));
        outState.putParcelableArrayList("files", new ArrayList<Uri>(fileList));
        outState.putStringArrayList("downloaded", new ArrayList<String>(downloadedList));

    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
