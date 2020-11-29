package com.example.doit.model;

public interface EditImageNicknameListener {
    public void onSkip(Runnable onFinish);
    public void onImageAndNickname(String nickname, String profileImage, Runnable onFinish);
}
