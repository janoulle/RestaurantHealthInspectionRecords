package com.janeullah.apps.healthinspectionviewer.models.heroku;

/**
 * @author Jane Ullah
 * @date 10/21/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class HerokuAppSleepingResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @SerializedName("exception")
    @Expose
    private Exception exception;

    /**
     * No args constructor for use in serialization
     *
     */
    public HerokuAppSleepingResponse() {
    }

    public HerokuAppSleepingResponse(Exception e){
        this.exception = e;
    }

    /**
     *
     * @param error
     * @param status
     */
    public HerokuAppSleepingResponse(String error, Integer status) {
        super();
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("error", error).append("status", status).toString();
    }
}



