package com.janeullah.apps.healthinspectionviewer.interfaces;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Jane Ullah
 * @date 4/30/2017.
 */

public interface Toastable {
    public void showToast(Context context,int resId, int duration);
    public void showToast(Context context, String message, int duration);
}
