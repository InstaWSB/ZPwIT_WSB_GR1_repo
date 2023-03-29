package com.zpwit_wsb_gr1_project.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zpwit_wsb_gr1_project.R;
import com.zpwit_wsb_gr1_project.adapter.HomeAdapter;
import com.zpwit_wsb_gr1_project.model.HomeModel;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {


    private RecyclerView recyclerView;
    HomeAdapter adapter;
    private List<HomeModel> list;
    Activity activity;
    private FirebaseUser user;

    DocumentReference  reference;

    public Home() {
        // Required empty public constructor
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

      //  reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());


        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
        
    }

    private void loadDataFromFirestore() {
    CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
        .document(user.getUid()).collection("Post Images");


    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
        {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}