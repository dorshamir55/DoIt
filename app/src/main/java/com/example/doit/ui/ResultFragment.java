package com.example.doit.ui;

import android.content.Context;
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

import com.example.doit.R;
import com.example.doit.adapter.ResultStatisticsRecyclerAdapter;
import com.example.doit.model.BackButtonListener;
import com.example.doit.model.ChangeLabelListener;
import com.example.doit.model.Statistic;

public class ResultFragment extends Fragment {
    private ResultStatisticsRecyclerAdapter adapter;
    private ChangeLabelListener changeLabelListener;
    private BackButtonListener backButtonListener;
    private Statistic statistic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ResultStatisticsRecyclerAdapter(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null)
            statistic = (Statistic) bundle.getSerializable("statistic");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.resultRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setData(statistic.getCompetitors());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            changeLabelListener = (ChangeLabelListener)context;
            backButtonListener = (BackButtonListener)context;
        } catch(ClassCastException ex) {
            throw new ClassCastException("NOTE! The activity must implement the fragment's listener" +
                    " interface!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        changeLabelListener.onChangeLabelVisibleListener();
        changeLabelListener.onChangeLabelTextListener(statistic.getTitle());
        backButtonListener.onBackButtonClickListener(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        changeLabelListener.onChangeLabelGoneListener();
        backButtonListener.onBackButtonClickListener(false);
    }
}