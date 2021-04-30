package com.team9.spda_team9.Introduction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.team9.spda_team9.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<DisplayItem> mListDisplay;

    public IntroViewPagerAdapter(Context mContext, List<DisplayItem> mDisplayList) {
        this.mContext = mContext;
        this.mListDisplay = mDisplayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);

        title.setText(mListDisplay.get(position).getTitle());
        description.setText(mListDisplay.get(position).getDescription());
        imgSlide.setImageResource(mListDisplay.get(position).getDisplayImg());

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListDisplay.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
