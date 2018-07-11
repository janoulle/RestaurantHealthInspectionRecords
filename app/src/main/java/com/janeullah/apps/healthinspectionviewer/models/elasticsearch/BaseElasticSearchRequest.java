package com.janeullah.apps.healthinspectionviewer.models.elasticsearch;

import com.janeullah.apps.healthinspectionviewer.interfaces.ElasticSearchRequestable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */
public abstract class BaseElasticSearchRequest implements ElasticSearchRequestable {
    protected Map<String, String> headers = new HashMap<>();
    protected final ElasticSearchRequest searchRequest = new ElasticSearchRequest();

    @Override
    public ElasticSearchRequest getPayload() {
        return searchRequest;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void populateSearchRequestObject(String searchValue) {
        Match matchTerm = new Match();
        matchTerm.setName(searchValue);
        ContainsMatchQuery containsMatchQuery = new ContainsMatchQuery();
        containsMatchQuery.setMatch(matchTerm);
        searchRequest.setContainsMatchQuery(containsMatchQuery);

        Sort nameSort = new Sort();
        nameSort.setNameKeyword(new NameKeyword("asc"));
        searchRequest.setSort(Collections.singletonList(nameSort));
        searchRequest.setSize(100);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("headers", headers)
                .append("payload", searchRequest)
                .toString();
    }
}
