package com.example.aplikasiseduhteh;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TehResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<TehModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<TehModel> getData() { return data; }
}