package com.janeullah.apps.healthinspectionviewer.async;

import com.janeullah.apps.healthinspectionviewer.listeners.ElasticSearchTaskListener;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */

public interface AsyncElasticSearchTaskable {
    void setElasticSearchListener(ElasticSearchTaskListener elasticSearchTaskListener);
}
