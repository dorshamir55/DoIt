package com.example.doit.model;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Patterns;

import com.example.doit.R;

import java.util.regex.Pattern;

public class Utils {
    public static boolean validateCredentials(String email, String password) {
        if(email == null || password == null)
            return false;

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;

        if(password.length() < 6)
            return false;

        return true;
    }

    public static boolean validateCredentials(String email, String password, String repeatPass) {
        return validateCredentials(email, password) && password.equals(repeatPass);
    }

    public static void replaceFragment (Fragment fragment, Context context){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = ((Activity)context).getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
