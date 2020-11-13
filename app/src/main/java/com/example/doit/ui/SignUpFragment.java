package com.example.doit.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.doit.R;
import com.example.doit.model.UserData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    public static final String TAG = "SIGN_UP_FRG";
    private SignUpFragmentClickListener listener;


    public SignUpFragment() {
        // Default Constructor for Android system..
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //EditText nicknameET = view.findViewById(R.id.nickname);
        EditText emailET = view.findViewById(R.id.email);
        EditText passwordET = view.findViewById(R.id.password);
        EditText repeatPassET = view.findViewById(R.id.passwordRepeat);
        ProgressBar loadingBar = view.findViewById(R.id.loadingBar);
        Button signupButton = view.findViewById(R.id.signup);

        signupButton.setOnClickListener(v -> {
            loadingBar.setVisibility(View.VISIBLE);
            listener.onSignUp("nickname", emailET.getText().toString(),
                    passwordET.getText().toString(), repeatPassET.getText().toString(),
                    () -> loadingBar.setVisibility(View.INVISIBLE));
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (SignUpFragmentClickListener)context;
        } catch(ClassCastException ex) {
            throw new ClassCastException("NOTE! The activity must implement the fragment's listener" +
                    " interface!");
        }
    }

    public static interface SignUpFragmentClickListener {
        public void onSignUp(String nickname, String email, String password, String repeatPass,
                             Runnable onFinish);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.action_sign_up));
    }
}