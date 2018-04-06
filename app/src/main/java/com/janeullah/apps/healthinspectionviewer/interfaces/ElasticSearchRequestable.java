package com.janeullah.apps.healthinspectionviewer.interfaces;

import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.ElasticSearchRequest;

import java.util.Map;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */
public interface ElasticSearchRequestable {
    void populateSearchRequestObject(String searchTerm);

    ElasticSearchRequest getPayload();

    Map<String, String> getHeaders();
}
