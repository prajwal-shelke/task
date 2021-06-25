package com.example.gallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.jsibbold.zoomage.ZoomageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    List<String> images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ZoomageView image;

        //Inflating viewPager_item.xml
        View itemView = inflater.inflate(R.layout.view_pager_item, container, false);

        image = itemView.findViewById(R.id.image_item);

        Glide.with(context).load(images.get(position)).into(image);

        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
