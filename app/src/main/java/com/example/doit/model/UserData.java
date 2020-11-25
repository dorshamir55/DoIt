package com.example.doit.model;


import com.example.doit.ui.EditImageNicknameFragment;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserData
{
    public static final String TABLE_NAME = "users";
    public static final String DEFAULT_IMAGE = "_none_profile_image.png";

    private String id;  // Auth uid + Firestore document id (Excluded)
    private String nickName;
    private String email;
    private String profileImageName;

    public UserData() {
    }

    public UserData(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
        this.profileImageName = DEFAULT_IMAGE;
    }

    public UserData withId(String id) {
        this.id = id;
        return this;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }
}

