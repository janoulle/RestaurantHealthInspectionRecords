package com.janeullah.apps.healthinspectionviewer.adapters;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/**
 * http://stackoverflow.com/questions/35809290/set-drawable-resource-id-in-androidsrc-for-imageview-using-data-binding-in-andr
 *
 * @author Jane Ullah
 * @date 5/2/2017.
 */
public class DataBindingAdapters {
    private DataBindingAdapters() {}

    @BindingAdapter("imageResource")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("imageResource")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("imageResource")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
