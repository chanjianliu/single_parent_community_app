package com.team9.spda_team9.Introduction;

public class DisplayItem {

    String Title,Description;
    int DisplayImg;

    public DisplayItem(String title, String description, int displayImg) {
        Title = title;
        Description = description;
        DisplayImg = displayImg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDisplayImg(int displayImg) {
        DisplayImg = displayImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getDisplayImg() {
        return DisplayImg;
    }
}
