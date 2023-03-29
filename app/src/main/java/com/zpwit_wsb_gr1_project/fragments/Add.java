package com.zpwit_wsb_gr1_project.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zpwit_wsb_gr1_project.R;
import com.zpwit_wsb_gr1_project.adapter.GalleryAdapter;
import com.zpwit_wsb_gr1_project.model.GalleryImages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Add extends Fragment {

    Uri imageUri;

    Dialog dialog;

    private EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ImageButton backBtn, nextBtn;
    private FirebaseUser user;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;

    public Add() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        init(view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        recyclerView.setAdapter(adapter);

        clickListener();
    }

    private void clickListener() {
        adapter.SendImage(new GalleryAdapter.SendImage() {
            @Override
            public void onSend(Uri picUri) {
                imageUri = picUri;




                Intent intent = CropImage
                        .activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4, 3)
                        .getIntent(getContext());



                cropActivityResult.launch(intent);


            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference().child("Post Images/"+System.currentTimeMillis());

                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadData(uri.toString());
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    ActivityResultLauncher<Intent> cropActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();

            CropImage.ActivityResult result1 = CropImage.getActivityResult(intent);
            Uri imageUri = result1.getUri();

            Glide.with(getContext()).load(imageUri).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    });

    private void uploadData(String imageURL) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid()).collection("Post Images");



        String description = String.valueOf(descET.getText());
        String id = reference.document().getId();;

        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        map.put("id", id);
        map.put("imageUrl", imageURL);
        map.put("timestamp", FieldValue.serverTimestamp());

        map.put("userName", user.getDisplayName());
        map.put("profileImage", String.valueOf(user.getPhotoUrl()));
        map.put("likeCount", 0);



        reference.document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    System.out.println();
                }
                else
                {
                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Dexter.withContext(getContext())
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted())
                    {

                        File file = new File(Environment.getExternalStorageDirectory().toString()+"/Download");
                        if (file.exists())
                        {

                            File[] files = file.listFiles();
                            assert files != null;
                            for (File file1 : files)
                            {
                                if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png"))
                                {
                                    list.add(new GalleryImages(Uri.fromFile(file1)));
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        }

                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                }
            }).check();
        }
    });
    }

    private void init(View view) {

        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.laoding_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dialog_bg, null));
        dialog.setCancelable(false);

    }
}