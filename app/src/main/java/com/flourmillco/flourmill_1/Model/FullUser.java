package com.flourmillco.flourmill_1.Model;

class FullUser {

    private Bakery bakery;
    private User user;

    public FullUser(Bakery bakery, User user) {
        this.bakery = bakery;
        this.user = user;
    }


    public Bakery getBakery() {
        return bakery;
    }

    public void setBakery(Bakery bakery) {
        this.bakery = bakery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
