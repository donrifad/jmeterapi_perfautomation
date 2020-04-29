package com.api.example.common;

import org.apache.http.HttpHeaders;

import java.util.HashMap;

public class Headers {

    public static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.put(HttpHeaders.ACCEPT, "application/json");
        return headers;
    }
}
