package com.zpwit_wsb_gr1_project.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zpwit_wsb_gr1_project.R;
import com.zpwit_wsb_gr1_project.adapter.HomeAdapter;
import com.zpwit_wsb_gr1_project.model.HomeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends Fragment {


    private RecyclerView recyclerView;
    HomeAdapter adapter;
    private List<HomeModel> list;
    Activity activity;
    private FirebaseUser user;
    Context context1;

    public Home() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        init(view);


        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();

        adapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(uid)
                        .collection("Post Images")
                        .document(id);

                if (likeList.contains(user.getUid()) ) {
                    likeList.remove(user.getUid()); // unlike
                } else {
                    likeList.add(user.getUid()); // like
                }

                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);

                reference.update(map);
            }

        });
        
    }

    private void loadDataFromFirestore() {

        final DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());

        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");


            reference.addSnapshotListener((value, error) -> {
                if (error != null) {
                    Log.d("Error: ", error.getMessage());
                }

                if (value == null)
                {
                    return;
                }

                List<String> uidList = (List<String>) value.get("following");

                if (uidList == null || uidList.isEmpty())
                {
                    return;
                }

                collectionReference.whereIn("uid", uidList).addSnapshotListener((value1, error1) -> {
                    if (error1 != null) {
                        Log.d("Error: ", error1.getMessage());
                    }

                    if (value1 == null)
                    {
                        return;

                    }

                    for (QueryDocumentSnapshot snapshot : value1) {

                        snapshot.getReference().collection("Post Images").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error2) {
                                if (error2 != null) {
                                    Log.d("Error: ", error2.getMessage());
                                }

                                if (value2 == null)
                                {
                                    return;
                                }
                                list.clear();
                                for (final QueryDocumentSnapshot snapshot1 : value2) {
                                    if (!snapshot1.exists())
                                    {
                                        return;
                                    }
                                    HomeModel model = snapshot1.toObject(HomeModel.class);

                                    list.add(new HomeModel(
                                            model.getName(),
                                            model.getProfileImage(),
                                            model.getImageUrl(),
                                            model.getUid(),
                                            model.getDescription(),
                                            model.getId(),
                                            model.getTimestamp(),
                                            model.getLikes()));



                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }

                });

            });

    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context1));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
        {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}