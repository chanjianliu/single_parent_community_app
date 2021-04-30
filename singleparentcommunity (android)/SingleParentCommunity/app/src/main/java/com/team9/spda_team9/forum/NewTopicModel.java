package com.team9.spda_team9.forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewTopicModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewTopicModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is new topic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
