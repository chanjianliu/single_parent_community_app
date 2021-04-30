package com.team9.spda_team9.forum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Subscription {
    private String userId;
    private List<Category> subscription = new ArrayList<>();

    public Subscription(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Category> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<Category> subscription) {
        this.subscription = subscription;
    }

    public void addSubscription(Category category){
        subscription.add(category);
    }

    public void removeSubscription(Category category){
        subscription.remove(category);
    }
}
