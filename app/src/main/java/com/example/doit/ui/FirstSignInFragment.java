package com.example.doit.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.doit.R;
import com.example.doit.adapter.AnswersRecyclerAdapter;
import com.example.doit.adapter.ChoosePictureAccountAdapter;
import com.example.doit.model.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirstSignInFragment extends Fragment {
    public static final String TAG = "FIRST_SIGN_IN_FRG";
    private FirstSignInFragmentClickListener listener;
    private ChoosePictureAccountAdapter adapter;

    private List<Task<Uri>> items;// = new ArrayList<>();
    private Button saveButton, skipButton;
    private EditText nicknameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new ArrayList<>();
        StorageReference reference = FirebaseStorage.getInstance().getReference("profile_pictures/");

        reference.listAll()
                .addOnSuccessListener(getActivity(), listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        items.add(item.getDownloadUrl());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

        adapter = new ChoosePictureAccountAdapter(items, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar loadingBar = view.findViewById(R.id.firstSignInLoadingBar);
        nicknameEditText = view.findViewById(R.id.nickname_edit_text);

        skipButton = view.findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                listener.onSkip(() -> loadingBar.setVisibility(View.INVISIBLE));
            }
        });

        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                String message = getResources().getString(R.string.nickname_validation);

                if(nickname.equals("")){
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingBar.setVisibility(View.VISIBLE);
                listener.onImageAndNickname(nickname,() -> loadingBar.setVisibility(View.INVISIBLE));
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.choosePicturesRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FirstSignInFragment.FirstSignInFragmentClickListener)context;
        } catch(ClassCastException ex) {
            throw new ClassCastException("NOTE! The activity must implement the fragment's listener" +
                    " interface!");
        }
    }

    public static interface FirstSignInFragmentClickListener {
        public void onSkip(Runnable onFinish);
        public void onImageAndNickname(String nickname, Runnable onFinish);
    }
}