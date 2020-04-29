package com.api.example.common;

public class Constant {


    public static final String TEST_ENV = System.getProperty("tests.env", "QA");
    public static final String TEST_RELEASE = System.getProperty("tests.release", "<Your Release>");
    public static final String TEST_PROJECT = System.getProperty("tests.project", "<Your Project>");
    public static final boolean UPDATE_DASHBOARD = Boolean.parseBoolean(System.getProperty("update.dashboard", "true"));

    Constant() {

    }


}
