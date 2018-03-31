package com.janeullah.apps.healthinspectionviewer.models;

import android.app.PendingIntent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * @author Jane Ullah
 * @date 5/1/2017.
 */
public class NotificationBuilderPojo {
    public int notificationId;
    public NotificationCompat.Style notificationStyle;
    public String contentTitle;
    public String contentText;
    public String bigText;
    public int smallIconResourceId;
    public int largeIconResourceId;
    public PendingIntent pendingIntent;

    public Bitmap getLargeIconBitmap(final Resources resources) {
        if (largeIconResourceId > 0) {
            return BitmapFactory.decodeResource(resources, largeIconResourceId);
        }
        return null;
    }
}
