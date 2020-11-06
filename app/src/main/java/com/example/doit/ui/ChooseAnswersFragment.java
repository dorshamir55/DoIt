package com.example.doit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doit.R;
import com.example.doit.adapter.AnswersRecyclerAdapter;
import com.example.doit.adapter.QuestionsRecyclerAdapter;
import com.example.doit.model.LocalHelper;
import com.example.doit.model.NewAnswer;
import com.example.doit.model.NewQuestion;
import com.example.doit.service.UploadPostService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startForegroundService;

public class ChooseAnswersFragment extends Fragment {
    private AnswersRecyclerAdapter adapter;
    private LocalHelper localHelper;

    private List<NewAnswer> answersList, checkedAnswersList;
    private NewQuestion question;
    private TextView questionTextView;
    private CheckBox answerCheckBox;
    private Button uploadButton;
    private int amountOfAnswers = 0;
    private final int MAX_ANSWERS = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localHelper = new LocalHelper(getActivity());
        adapter = new AnswersRecyclerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_answers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionTextView = view.findViewById(R.id.selected_question);
        uploadButton = view.findViewById(R.id.upload_button);
        checkedAnswersList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            question = (NewQuestion) bundle.getSerializable("question");
            String questionText = null;
            if(localHelper.getLocale().equals("en"))
                questionText = question.getEn().getQuestionText();
            else if(localHelper.getLocale().equals("he"))
                questionText = question.getHe().getQuestionText();
            questionTextView.setText(questionText);
        }
        answersList = question.getAnswers();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountOfAnswers !=4){
                    Toast.makeText(getActivity(), getResources().getString(R.string.valid_answers_message), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), UploadPostService.class);
                Bundle bundleToService = new Bundle();
                bundleToService.putSerializable("question", question);
//                bundleToService.putParcelable("answers", (Parcelable) checkedAnswersList);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    getActivity().startForegroundService(intent);
                else
                    getActivity().startService(intent);

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment());
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.answersRecycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setData(answersList);
        adapter.notifyDataSetChanged();

        adapter.setRecyclerListener(new AnswersRecyclerAdapter.AnswersRecyclerListener() {
            @Override
            public void onCheckChange(int position, boolean isChecked, CompoundButton buttonView, NewAnswer clickedAnswer) {
                View view = (View) buttonView.getParent();
                if(isChecked) {
                    if(amountOfAnswers>=MAX_ANSWERS) {
                        buttonView.setChecked(false);
                        Toast.makeText(getActivity(), getResources().getString(R.string.max_answers_message), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        view.setBackgroundColor(getResources().getColor(R.color.toolbarColorVeryLight));
                        checkedAnswersList.add(clickedAnswer);
                        amountOfAnswers++;
                    }
                }
                else{
                    view.setBackgroundColor(Color.WHITE);
                    checkedAnswersList.remove(clickedAnswer);
                    amountOfAnswers--;
                }
            }
        });
    }
}