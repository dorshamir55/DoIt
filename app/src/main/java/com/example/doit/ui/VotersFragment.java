package com.example.doit.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.adapter.UserSearchRecyclerAdapter;
import com.example.doit.adapter.UserVotersRecyclerAdapter;
import com.example.doit.model.AnswerInPost;
import com.example.doit.model.BackButtonListener;
import com.example.doit.model.Consumer;
import com.example.doit.model.UserData;
import com.example.doit.model.UserProfileListener;
import com.example.doit.viewmodel.IMainViewModel;
import com.example.doit.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class VotersFragment extends Fragment {
    private IMainViewModel viewModel = null;
    private BackButtonListener backButtonListener;
    private UserProfileListener userProfileListener;
    private UserVotersRecyclerAdapter adapter;
    private List<AnswerInPost> answersInPost;
    private List<UserData> userDataList;

    public VotersFragment(List<AnswerInPost> answersInPost) {
        this.answersInPost = new ArrayList<>(answersInPost);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        adapter = new UserVotersRecyclerAdapter(getActivity());
        userDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.votersRecycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        adapter.setRecyclerListener(new UserVotersRecyclerAdapter.UserVotersRecyclerListener() {
            @Override
            public void onItemClick(int position, View clickedView, String userID) {
                userProfileListener.onClickUserProfile(userID);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        backButtonListener.onBackButtonClickListener(true);

//        Consumer<List<UserData>> consumerList = new Consumer<List<UserData>>() {
//            @Override
//            public void apply(List<UserData> users) {
//                userDataList = users;
//                adapter.setData(userDataList);
//                adapter.notifyDataSetChanged();
//            }
//        };
//
//        viewModel.getAllUsers(consumerList);
        Consumer<List<UserData>> consumer = new Consumer<List<UserData>>() {
            @Override
            public void apply(List<UserData> users) {
                userDataList = users;
                adapter.setData(userDataList);
                adapter.notifyDataSetChanged();
            }
        };
        viewModel.getUsersByIds(answersInPost, consumer);
    }

    @Override
    public void onPause() {
        super.onPause();

        backButtonListener.onBackButtonClickListener(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            backButtonListener = (BackButtonListener)context;
            userProfileListener = (UserProfileListener)context;
        } catch(ClassCastException ex) {
            throw new ClassCastException("NOTE! The activity must implement the fragment's listener" +
                    " interface!");
        }
    }
}