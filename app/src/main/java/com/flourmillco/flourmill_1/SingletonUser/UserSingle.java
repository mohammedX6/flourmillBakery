package com.flourmillco.flourmill_1.SingletonUser;

import android.app.Application;

import com.flourmillco.flourmill_1.Location.User;


public class UserSingle extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
