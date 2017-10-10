package com.janeullah.apps.healthinspectionviewer;

import android.content.Context;
import android.content.SharedPreferences;

import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.utils.UUIDInitializer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * https://developer.android.com/training/testing/unit-testing/local-unit-tests.html#build
 * @author Jane Ullah
 * @date 10/9/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UUIDInitializerTest {

    @Mock
    BaseActivity mockBaseActivity;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    SharedPreferences.Editor mockSharedPreferencesEditor;

    @Test
    public void generateUUIDinstance() {
        //mock shared preference
        when(mockBaseActivity.getSharedPreferences(AppConstants.UUID_KEY, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);

        //mock actions on the shared preference
        when(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor);
        when(mockSharedPreferencesEditor.putString(anyString(),anyString())).thenReturn(mockSharedPreferencesEditor);
        doNothing().when(mockSharedPreferencesEditor).apply();

        UUIDInitializer uuidInitializer = UUIDInitializer.getInstance(mockBaseActivity);
        String firstUUIDValue = uuidInitializer.getUUID();
        uuidInitializer = UUIDInitializer.getInstance(mockBaseActivity);
        String secondUUIDValue = uuidInitializer.getUUID();

        assertThat(firstUUIDValue,is(secondUUIDValue));
        verify(mockSharedPreferencesEditor, times(1)).apply();
    }
}