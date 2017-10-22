package com.janeullah.apps.healthinspectionviewer.interfaces;

import com.google.gson.Gson;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */

public interface TaskListenable<T,U> {
    Gson gson = new Gson();
    T onSuccess(U arg);
}
