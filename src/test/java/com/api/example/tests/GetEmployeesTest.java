package com.api.example.tests;

import com.api.example.common.Headers;
import com.api.example.util.RequestUtil;
import com.api.example.util.ResponseUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.api.example.util.RequestUtil.API_HOST;
import static com.api.example.util.RequestUtil.BASE_PATH;

public class GetEmployeesTest {


    @BeforeClass
    public static void initiate(ITestContext iTestContext) {
        iTestContext.setAttribute("feature", "Employees - Get Employees");
    }

    @Test(description = "ID-001", alwaysRun = true)
    public static void testBasicRequest() {
        API_HOST = "http://dummy.restapiexample.com/api/v1";
        BASE_PATH = "/employees";
        Response response = RequestUtil.send(Headers.getHeader(), "", "", "GET", null);
        Assert.assertEquals(ResponseUtil.getResponseStatus(response), true);

    }

}
