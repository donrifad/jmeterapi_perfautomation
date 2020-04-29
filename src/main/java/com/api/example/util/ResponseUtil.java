package com.api.example.util;

import io.restassured.response.Response;

/**
 * QE-CORE CONTAINS API RELATED FUNCTIONS
 * Refer  com.syscolabs.qe.core.api
 */
public class ResponseUtil {

    public static boolean getResponseStatus(String response) {
        boolean isSuccess = false;
        try {
            String status = getValue(response, "success");
            if (!status.isEmpty())
                isSuccess = Boolean.parseBoolean(status);
            else
                isSuccess = getValue(response, "status").equals("success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return isSuccess;
        }
    }

    public static boolean getResponseStatus(Response response) {
        boolean isSuccess = false;
        try {
            String status = getValue(response, "success");
            if (!status.isEmpty())
                isSuccess = Boolean.parseBoolean(status);
            else
                isSuccess = getValue(response, "status").equals("success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return isSuccess;
        }
    }

    public static String getValue(Response response, String key) {
        return getValue(response.asString(), key);
    }

    public static String getValue(String response, String key) {
        String value = "";
        try {
            if (response.charAt(0) != '{' && response.charAt(response.length() - 1) != '}') {
                response = response.replace(response.substring(0, 1), "");
                response = response.replace(response.substring(response.length() - 1, response.length()), "");
            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = responseBody.getString(key);
            } catch (Exception e) {

            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = String.valueOf(responseBody.getBoolean(key));
            } catch (Exception e) {

            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = String.valueOf(responseBody.getInt(key));
            } catch (Exception e) {

            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = String.valueOf(responseBody.getDouble(key));
            } catch (Exception e) {

            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = String.valueOf(responseBody.getJSONObject(key));
            } catch (Exception e) {

            }
            try {
                org.json.JSONObject responseBody = new org.json.JSONObject(response);
                value = String.valueOf(responseBody.getJSONArray(key));
            } catch (Exception e) {

            }
        } finally {
            return value;
        }
    }

}