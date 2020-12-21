package com.example.doit.remote;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.doit.R;
import com.example.doit.model.AnswerFireStore;
import com.example.doit.model.AnswerInPost;
import com.example.doit.model.AnswerInQuestion;
import com.example.doit.model.Consumer;
import com.example.doit.model.QuestionFireStore;
import com.example.doit.model.QuestionPostData;
import com.example.doit.model.StatisticElement;
import com.example.doit.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainRemoteDataSource implements IMainRemoteDataSource {

    private FirebaseFirestore db;

    public MainRemoteDataSource() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void fetchQuestionsPosts(Date fromDate, Consumer<List<QuestionPostData>> consumer) {
        Query query = db.collection(QuestionPostData.TABLE_NAME).orderBy("updateDate", Query.Direction.DESCENDING);

        if(fromDate != null)
            query = query.whereGreaterThan("updateDate", fromDate);

        query.get()
                .addOnCompleteListener(task -> {
                    List<QuestionPostData> data = null;
                    if (task.isSuccessful()) {
                        data = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            //ArrayList<Answer> answers = new ArrayList<>();
                            //ArrayList<Answer> answers = (ArrayList<Answer>) document.get("answers");
                            //Log.d(TAG, list.toString());
                            data.add(document.toObject(QuestionPostData.class).withId(document.getId()));
                            //data.get(data.size()-1).setAnswers(answers);
                        }
                    } else {
                        task.getException().printStackTrace();
                    }

                    if(consumer != null)
                        consumer.apply(data);
                });
    }

    @Override
    public void removePost(String id, Runnable onFinish) {
        Map<String, Object> data = new HashMap<>();
        data.put("updateDate", FieldValue.serverTimestamp());
        data.put("removed", true);
        db.collection(QuestionPostData.TABLE_NAME).document(id).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(onFinish != null)
                            onFinish.run();
                    }
                });
    }

    @Override
    public void fetchAllQuestions(Consumer<List<QuestionFireStore>> consumerList) {
        Query query = db.collection(QuestionFireStore.TABLE_NAME);
        query.get()
                .addOnCompleteListener(task -> {
                    List<QuestionFireStore> data = null;
                    if (task.isSuccessful()) {
                        data = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            data.add(document.toObject(QuestionFireStore.class).withId(document.getId()));
                        }
                    } else {
                        task.getException().printStackTrace();
                    }

                    if(consumerList != null)
                        consumerList.apply(data);
                });
    }
    public void prepareStatistics(Consumer<List<StatisticElement>> consumerList, List<StatisticElement> statisticElements){
        List<StatisticElement>competitors = new ArrayList<>();
        long currentMaxChoices = statisticElements.get(0).getValue();
        int degree = 1;
        for(StatisticElement statisticElement : statisticElements) {
            String imagePath = null;
            if(statisticElement.getValue() < currentMaxChoices){
                degree++;
                currentMaxChoices = statisticElement.getValue();
            }
            if(degree == 1){
                imagePath = String.valueOf(R.drawable.gold_medal);
            }
            else if (degree == 2){
                imagePath = String.valueOf(R.drawable.silver_medal);
            }
            else if(degree == 3) {
                imagePath = String.valueOf(R.drawable.bronze_medal);
            }
            String stringImageUri = "android.resource://com.example.doit/" + imagePath;
//                    Uri imageUri = Uri.parse("android.resource://com.example.doit/" + imagePath);
//            StatisticElement statisticElement = new StatisticElement(questionFireStore.getTextByLanguage(currentLanguage), String.valueOf(questionFireStore.getAmountOfChoices()), stringImageUri, degree);
            statisticElement.setStringImageUri(stringImageUri);
            statisticElement.setPosition(degree);
            competitors.add(statisticElement);
        }

        consumerList.apply(competitors);
    }
    public void fetchTopQuestions(Consumer<List<QuestionFireStore>> consumerList, int topQuestion) {
        Consumer<List<QuestionFireStore>> consumer = new Consumer<List<QuestionFireStore>>() {
            @Override
            public void apply(List<QuestionFireStore> questionsList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<QuestionFireStore> topQuestionsList;
                    questionsList.sort(Comparator.comparing(QuestionFireStore::getAmountOfChoices));
                    Collections.reverse(questionsList);
                    int amountOfQuestionsInRange = 1, topQuestionToDisplay = topQuestion;
                    long differentChoicesOfLastQuestion = 1;
                    if(topQuestion >= questionsList.size()) {
                        topQuestionsList = questionsList;
                    }
                    else
                    {
                        while(amountOfQuestionsInRange < questionsList.size() && differentChoicesOfLastQuestion < topQuestion)
                        {
                            if(questionsList.get(amountOfQuestionsInRange-1).getAmountOfChoices() > questionsList.get(amountOfQuestionsInRange).getAmountOfChoices())
                                differentChoicesOfLastQuestion++;
                            amountOfQuestionsInRange++;
                        }

                        if(differentChoicesOfLastQuestion >= topQuestion && amountOfQuestionsInRange < questionsList.size()) {
                            while (questionsList.get(amountOfQuestionsInRange-1).getAmountOfChoices() == questionsList.get(amountOfQuestionsInRange).getAmountOfChoices())
                            {
                                amountOfQuestionsInRange++;
                                if(amountOfQuestionsInRange >= questionsList.size())
                                    break;
                            }
                        }

                        topQuestionsList = questionsList.subList(0, amountOfQuestionsInRange);
                    }

                    consumerList.apply(topQuestionsList);
                }
            }
        };

        fetchAllQuestions(consumer);
    }

    @Override
    public void fetchTopUsersInPosts(Consumer<List<UserData>> topUsersPostsConsumer, int topUsersPosts) {
        Consumer<List<UserData>> consumer = new Consumer<List<UserData>>() {
            @Override
            public void apply(List<UserData> usersList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<UserData> topUsersList;
                    usersList.sort(Comparator.comparing(UserData::getAmountOfPostedQuestionPostsIdList));
                    Collections.reverse(usersList);
                    int amountOfUsersInRange = 1, topQuestionToDisplay = topUsersPosts;
                    long differentAmountOfLastUser = 1;
                    if(topUsersPosts >= usersList.size()) {
                        topUsersList = usersList;
                    }
                    else
                    {
                        while(amountOfUsersInRange < usersList.size() && differentAmountOfLastUser < topUsersPosts)
                        {
                            if(usersList.get(amountOfUsersInRange-1).getAmountOfPostedQuestionPostsIdList() > usersList.get(amountOfUsersInRange).getAmountOfPostedQuestionPostsIdList())
                                differentAmountOfLastUser++;
                            amountOfUsersInRange++;
                        }

                        if(differentAmountOfLastUser >= topUsersPosts && amountOfUsersInRange < usersList.size()) {
                            while (usersList.get(amountOfUsersInRange-1).getAmountOfPostedQuestionPostsIdList() == usersList.get(amountOfUsersInRange).getAmountOfPostedQuestionPostsIdList())
                            {
                                amountOfUsersInRange++;
                                if(amountOfUsersInRange >= usersList.size())
                                    break;
                            }
                        }

                        topUsersList = usersList.subList(0, amountOfUsersInRange);
                    }

                    topUsersPostsConsumer.apply(topUsersList);
                }
            }
        };

        fetchAllUsers(consumer);
    }

    @Override
    public void fetchTopUsersInVotes(Consumer<List<UserData>> topUsersVotesConsumer, int topUsersVotes) {
        Consumer<List<UserData>> consumer = new Consumer<List<UserData>>() {
            @Override
            public void apply(List<UserData> usersList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<UserData> topUsersList;
                    usersList.sort(Comparator.comparing(UserData::getAmountOfVotedQuestionPostsIdList));
                    Collections.reverse(usersList);
                    int amountOfUsersInRange = 1, topQuestionToDisplay = topUsersVotes;
                    long differentAmountOfLastUser = 1;
                    if(topUsersVotes >= usersList.size()) {
                        topUsersList = usersList;
                    }
                    else
                    {
                        while(amountOfUsersInRange < usersList.size() && differentAmountOfLastUser < topUsersVotes)
                        {
                            if(usersList.get(amountOfUsersInRange-1).getAmountOfVotedQuestionPostsIdList() > usersList.get(amountOfUsersInRange).getAmountOfVotedQuestionPostsIdList())
                                differentAmountOfLastUser++;
                            amountOfUsersInRange++;
                        }

                        if(differentAmountOfLastUser >= topUsersVotes && amountOfUsersInRange < usersList.size()) {
                            while (usersList.get(amountOfUsersInRange-1).getAmountOfVotedQuestionPostsIdList() == usersList.get(amountOfUsersInRange).getAmountOfVotedQuestionPostsIdList())
                            {
                                amountOfUsersInRange++;
                                if(amountOfUsersInRange >= usersList.size())
                                    break;
                            }
                        }

                        topUsersList = usersList.subList(0, amountOfUsersInRange);
                    }

                    topUsersVotesConsumer.apply(topUsersList);
                }
            }
        };

        fetchAllUsers(consumer);
    }

    @Override
    public void fetchAllUsers(Consumer<List<UserData>> consumerList) {
        Query query = db.collection(UserData.TABLE_NAME);
        query.get()
                .addOnCompleteListener(task -> {
                    List<UserData> data = null;
                    if (task.isSuccessful()) {
                        data = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            data.add(document.toObject(UserData.class).withId(document.getId()));
                        }
                    } else {
                        task.getException().printStackTrace();
                    }
                    if(consumerList != null)
                        consumerList.apply(data);
                });
    }

    @Override
    public void fetchUsersByIds(List<AnswerInPost> answersInPost, Consumer<List<UserData>> consumerList) {
        List<UserData> users = new ArrayList<>();
        for(AnswerInPost answerInPost : answersInPost){
            for(String votedUserId : answerInPost.getVotedUserIdList()){
                Consumer<UserData> userConsumer = new Consumer<UserData>() {
                    @Override
                    public void apply(UserData currentUser) {
                        UserData userData = currentUser;
                        users.add(userData);
                        if(consumerList != null)
                            consumerList.apply(users);
                    }
                };
                getCurrentUserData(votedUserId, userConsumer);
            }
        }
    }

    @Override
    public void fetchAnswers(Consumer<List<AnswerFireStore>> consumerList, List<AnswerInQuestion> answerInQuestions) {
        Query query = db.collection(AnswerFireStore.TABLE_NAME);
        query.get()
                .addOnCompleteListener(task -> {
                    List<AnswerFireStore> data = null;
                    if (task.isSuccessful()) {
                        data = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(answerInQuestions != null) {
                            for (AnswerInQuestion answer : answerInQuestions) {
                                for (DocumentSnapshot document : task.getResult().getDocuments())
                                    if (document.getId().equals(answer.getAnswerID()))
                                        data.add(document.toObject(AnswerFireStore.class).withId(document.getId()));
                            }
                        }
                    } else {
                        task.getException().printStackTrace();
                    }

                    if(consumerList != null)
                        consumerList.apply(data);
                });
    }

    @Override
    public void updateVotes(String questionPostId, String currentUserId, List<AnswerInPost> answersInPost, List<String> votedQuestionPostsIdList, int votedPosition, Runnable onFinish) {
        Map<String, Object> data = new HashMap<>();
        List<String> votedList = answersInPost.get(votedPosition).getVotedUserIdList();
        votedList.add(currentUserId);
        answersInPost.get(votedPosition).setVotedUserIdList(votedList);
        data.put("updateDate", FieldValue.serverTimestamp());
        data.put("voted", true);
        data.put("answers", answersInPost);
        db.collection(QuestionPostData.TABLE_NAME).document(questionPostId).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        votedQuestionPostsIdList.add(questionPostId);
                        data.clear();
                        data.put("votedQuestionPostsIdList", votedQuestionPostsIdList);
                        db.collection(UserData.TABLE_NAME).document(currentUserId).set(data, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(onFinish != null)
                                            onFinish.run();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void endingPostDate(String id, Runnable onFinish) {
        Map<String, Object> data = new HashMap<>();
        data.put("updateDate", FieldValue.serverTimestamp());
        data.put("postTimeOver", true);
        db.collection(QuestionPostData.TABLE_NAME).document(id).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(onFinish != null)
                            onFinish.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void changeUpdatedToFalse(String id) {
        Map<String, Object> data = new HashMap<>();
        data.put("voted", false);
        db.collection(QuestionPostData.TABLE_NAME).document(id).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void incrementAnswerWins(String questionID, List<String> winners) {
        Consumer<List<AnswerInQuestion>> consumer = new Consumer<List<AnswerInQuestion>>() {
            @Override
            public void apply(List<AnswerInQuestion> allAnswersInQuestion) {
                for(String winnerID : winners){
                    for(int i=0; i<allAnswersInQuestion.size(); i++){
                        if(winnerID.equals(allAnswersInQuestion.get(i).getAnswerID())){
                            AnswerInQuestion answerInQuestion = allAnswersInQuestion.get(i);
                            answerInQuestion.setAmountOfWins(answerInQuestion.getAmountOfWins()+1);
                            allAnswersInQuestion.set(i ,answerInQuestion);
                        }
                    }
                }
                db.collection(QuestionFireStore.TABLE_NAME).document(questionID)
                        .update("answersInQuestion", allAnswersInQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        };
        fetchAllAnswersInQuestion(questionID, consumer);
    }

    @Override
    public void getCurrentUserData(String uid, Consumer<UserData> userConsumer) {
        db.collection(UserData.TABLE_NAME).document(uid)
                .get().addOnCompleteListener(task -> {
            UserData userData = null;
            if(task.isSuccessful()){
                userData = new UserData();
                DocumentSnapshot documentSnapshot = task.getResult();
                userData = documentSnapshot.toObject(UserData.class).withId(uid);
            } else {
                task.getException().printStackTrace();
            }
            if(userConsumer != null)
                userConsumer.apply(userData);
        });
    }

    @Override
    public void fetchAllAccountImages(Consumer<List<Uri>> uriConsumer) {
        StorageReference reference = FirebaseStorage.getInstance().getReference("profile_pictures/");
        List<Uri> uriList = new ArrayList<>();

        reference.listAll()
                .addOnSuccessListener( listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uriList.add(uri);
                            }
                        });
                    }
//                    if(uriConsumer!=null)
                    uriConsumer.apply(uriList);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void decrementAmountOfChosenQuestionInQuestionPost(String questionID) {
        FirebaseFirestore.getInstance().collection(QuestionFireStore.TABLE_NAME).document(questionID)
                .update("amountOfChoices", FieldValue.increment(-1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void deleteQuestionPostIdFromUser(String questionPostID, String userID, List<String> postedQuestionPostsIdList, Runnable onFinish) {
        Map<String, Object> data = new HashMap<>();
        postedQuestionPostsIdList.remove(questionPostID);
        data.put("postedQuestionPostsIdList", postedQuestionPostsIdList);
        db.collection(UserData.TABLE_NAME).document(userID).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(onFinish != null)
                            onFinish.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void decrementVotesOfVoters(String questionPostID, List<AnswerInPost> answersInPost) {
        for(AnswerInPost answerInPost : answersInPost){
            for(String votedUserId : answerInPost.getVotedUserIdList()){

                Consumer<UserData> userConsumer = new Consumer<UserData>() {
                    @Override
                    public void apply(UserData currentUser) {
                        List<String> votedQuestionPostIdList = currentUser.getVotedQuestionPostsIdList();
                        votedQuestionPostIdList.remove(questionPostID);

                        Map<String, Object> data = new HashMap<>();
                        data.put("votedQuestionPostsIdList", votedQuestionPostIdList);
                        db.collection(UserData.TABLE_NAME).document(votedUserId).set(data, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                };
                getCurrentUserData(votedUserId, userConsumer);

            }
        }
    }

    private void fetchAllAnswersInQuestion(String questionID, Consumer<List<AnswerInQuestion>> consumerList) {

        db.collection(QuestionFireStore.TABLE_NAME).document(questionID)
                .get().addOnCompleteListener(task -> {
            List<AnswerInQuestion> data = null;
            if (task.isSuccessful()) {
                data = new ArrayList<>();
                DocumentSnapshot documentSnapshot = task.getResult();
                data.addAll(documentSnapshot.toObject(QuestionFireStore.class).getAnswersInQuestion());
            } else {
                task.getException().printStackTrace();
            }
            if(consumerList != null)
                consumerList.apply(data);
        });
    }
}
