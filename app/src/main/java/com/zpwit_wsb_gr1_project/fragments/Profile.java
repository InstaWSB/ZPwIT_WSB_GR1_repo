package com.zpwit_wsb_gr1_project.fragments;

import static com.zpwit_wsb_gr1_project.MainActivity.IS_SEARCHED_USER;
import static com.zpwit_wsb_gr1_project.MainActivity.USER_ID;
import static com.zpwit_wsb_gr1_project.utils.Constants.PREF_DIRECTORY;
import static com.zpwit_wsb_gr1_project.utils.Constants.PREF_NAME;
import static com.zpwit_wsb_gr1_project.utils.Constants.PREF_STORED;
import static com.zpwit_wsb_gr1_project.utils.Constants.PREF_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zpwit_wsb_gr1_project.FragmentReplacerActivity;
import com.zpwit_wsb_gr1_project.MainActivity;
import com.zpwit_wsb_gr1_project.R;
import com.zpwit_wsb_gr1_project.model.PostImageModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment   {
    private TextView nameTv, toolbarNameTv, statusTv, followingCountTv, followersCountTv, postCountTv;
    private CircleImageView profileImage;
    private Button followBtn, startChatBtn;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private LinearLayout countLayout;
    boolean isMyProfile = true;
    private ImageButton editProfileBtn;
    private String uid;
    String userUID;
    List<String> followersList;
    List<String> followingList;
    List<String> followingList_2;
    boolean isFollowed;
    int count;
    Context context1;
    private  DocumentReference userRef, myRef;
    FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    Animation scaleUp, scaleDown;
    AnimationSet s;
    ImageButton sendButton;
    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context1 = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        myRef = FirebaseFirestore.getInstance().collection("Users").
                document(user.getUid());
        if (IS_SEARCHED_USER) {
            isMyProfile = false;
            userUID = USER_ID;

            loadData();

        } else {
            isMyProfile = true;
            userUID = user.getUid();
        }
        if (isMyProfile) {
            editProfileBtn.setVisibility(View.VISIBLE);
            followBtn.setVisibility(View.GONE);
            countLayout.setVisibility(View.VISIBLE);
            startChatBtn.setVisibility(View.GONE);

        } else {
            followBtn.setVisibility(View.VISIBLE);
          //  countLayout.setVisibility(View.GONE);
            startChatBtn.setVisibility(View.VISIBLE);
            editProfileBtn.setVisibility(View.GONE);
        }


        userRef = FirebaseFirestore.getInstance().collection("Users").document(userUID);

        loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context1, 3));

        loadPostImages();

        recyclerView.setAdapter(adapter);

        clickListener();

    }

    private void loadData() {
        myRef.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.e("Tag_b", error.getMessage());
                return;
            }

            if (value == null || !value.exists()) {
                return;
            }

            followingList_2 = (List<String>) value.get("following");


        });
    }

    private void  clickListener()
    {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(s);
                Intent intent = new Intent(context1, FragmentReplacerActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                getActivity().finish();
            }
        });


        followBtn.setOnClickListener(v -> {
            v.startAnimation(s);
            if (isFollowed) {

                followersList.remove(user.getUid()); //opposite user

                followingList_2.remove(userUID); //us

                final Map<String, Object> map_2 = new HashMap<>();
                map_2.put("following", followingList_2);


                Map<String, Object> map = new HashMap<>();
                map.put("followers", followersList);


                userRef.update(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        followBtn.setText(context1.getResources().getString(R.string.follow));

                        myRef.update(map_2).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(context1,context1.getResources().getString(R.string.unFollowed), Toast.LENGTH_SHORT).show();
                            } else {
                                assert task1.getException() != null;
                                Log.e("Tag_3", task1.getException().getMessage());
                            }
                        });

                    } else {
                        assert task.getException() != null;
                        Log.e("Tag", "" + task.getException().getMessage());
                    }
                });


            } else {



                followersList.add(user.getUid()); //opposite user

                followingList_2.add(userUID); //us

                final Map<String, Object> map_2 = new HashMap<>();
                map_2.put("following", followingList_2);


                Map<String, Object> map = new HashMap<>();
                map.put("followers", followersList);


                userRef.update(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        followBtn.setText(context1.getResources().getString(R.string.unFollow));

                        myRef.update(map_2).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                Toast.makeText(context1, context1.getResources().getString(R.string.followed), Toast.LENGTH_SHORT).show();
                            } else {
                                assert task12.getException() != null;
                                Log.e("tag_3_1", task12.getException().getMessage());
                            }
                        });


                    } else {
                        assert task.getException() != null;
                        Log.e("Tag", "" + task.getException().getMessage());
                    }
                });


            }

        });




        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(s);
                Intent intent = CropImage
                        .activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .getIntent(context1);



                profileImageresult.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> profileImageresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();

            CropImage.ActivityResult result1 = CropImage.getActivityResult(intent);
            if (result1 == null)
                return;
            Uri imageUri = result1.getUri();

           uploadImage(imageUri);
        }
    });

    private void uploadImage(Uri imageUri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                            request.setPhotoUri(uri);

                            user.updateProfile(request.build());

                            Map<String, Object> map = new HashMap<>();
                            map.put("profileImage", imageURL);

                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(user.getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful())
                               {
                                   Toast.makeText(context1, context1.getResources().getString(R.string.updatedSuccesfull), Toast.LENGTH_SHORT).show();
                               }
                               else
                               {
                                   Toast.makeText(context1, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }
                                }
                            });
                        }
                    });
                }
                else
                {
                    Toast.makeText(context1, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadBasicData() {

        userRef.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.e("Tag_0", error.getMessage());
                return;
            }

            assert value != null;
            if (value.exists()) {

                String name = value.getString("name");
                String status = value.getString("status");

                final String profileURL = value.getString("profileImage");

                nameTv.setText(name);
                toolbarNameTv.setText(name);
                statusTv.setText(status);

                followersList = (List<String>) value.get("followers");
                followingList = (List<String>) value.get("following");


                followersCountTv.setText("" + followersList.size());
                followingCountTv.setText("" + followingList.size());

                try {


                    Glide.with(context1.getApplicationContext())
                            .load(profileURL)
                            .placeholder(R.drawable.ic_person)
                            .circleCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                    storeProfileImage(bitmap, profileURL);
                                    return false;
                                }
                            })
                            .timeout(6500)
                            .into(profileImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (followersList.contains(user.getUid())) {
                    followBtn.setText(context1.getResources().getString(R.string.unFollow));
                    isFollowed = true;
                    startChatBtn.setVisibility(View.VISIBLE);


                } else {
                    isFollowed = false;
                    followBtn.setText(context1.getResources().getString(R.string.follow));

                    startChatBtn.setVisibility(View.GONE);

                }


            }

        });

    }

    private void storeProfileImage(Bitmap bitmap, String url) {

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isStored = preferences.getBoolean(PREF_STORED, false);
        String urlString = preferences.getString(PREF_URL, "");

        SharedPreferences.Editor editor = preferences.edit();

        if (isStored && urlString.equals(url))
            return;

        if (IS_SEARCHED_USER)
            return;

        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());

        File directory = contextWrapper.getDir("image_data", Context.MODE_PRIVATE);

        if (!directory.exists()) {
            boolean isMade = directory.mkdirs();
            Log.d("Directory", String.valueOf(isMade));
        }


        File path = new File(directory, "profile.png");

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        editor.putBoolean(PREF_STORED, true);
        editor.putString(PREF_URL, url);
        editor.putString(PREF_DIRECTORY, directory.getAbsolutePath());
        editor.apply();
    }

    private void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        followersCountTv = view.findViewById(R.id.followersCountTv);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        postCountTv = view.findViewById(R.id.postCountTv);
        profileImage = view.findViewById(R.id.profileImage);
        followBtn = view.findViewById(R.id.followBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        startChatBtn = view.findViewById(R.id.startChatBtn);
        countLayout = view.findViewById(R.id.countLayout);
        editProfileBtn = view.findViewById(R.id.edit_profileImage);
        sendButton = view.findViewById(R.id.sendBtn);

        scaleUp = AnimationUtils.loadAnimation(context1, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(context1, R.anim.scale_down);
        s = new AnimationSet(false);//false means don't share interpolators
        s.addAnimation(scaleDown);
        s.addAnimation(scaleUp);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void loadPostImages() {



            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection("Users").document(userUID);

        Query query = reference.collection("Post Images");
        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class).build();

         adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);
                return new PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {

                Random random = new Random();

                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                Glide.with(holder.itemView.getContext().getApplicationContext())
                        .load(model.getImageUrl()).placeholder(new ColorDrawable(color))
                        .timeout(6500)
                        .into(holder.imageView);
                count = getItemCount();
                postCountTv.setText("" + count);
            }

             @Override
             public int getItemCount() {
                 return super.getItemCount();
             }

        };





        }



    private static class PostImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}