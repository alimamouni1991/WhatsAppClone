package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("G6vr8aS3I5jcNZ3sHcJz0ct1x5rJI7pqwKYysVfG")
                // if defined
                .clientKey("nds1GCCC856IqiQMjZ5q4CC5ebfS4sg9Lz7ruQ0S")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
