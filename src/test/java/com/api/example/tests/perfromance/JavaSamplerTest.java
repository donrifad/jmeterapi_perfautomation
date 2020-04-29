package com.api.example.tests.perfromance;

import com.api.example.tests.GetEmployeesTest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.testng.annotations.Test;

public class JavaSamplerTest extends AbstractJavaSamplerClient {


    public static int cnt = 0;

    @Test
    public void setupTest(JavaSamplerContext javaSamplerContext) {

    }

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();

        result.sampleStart();

        GetEmployeesTest loginTest = new GetEmployeesTest();

        try {
            loginTest.testBasicRequest();
            result.sampleEnd();
            result.setResponseCode("200");
            result.setResponseMessage("OK");
            result.setSuccessful(true);

        } catch (NullPointerException e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(true);
            e.printStackTrace();

        } catch (Exception e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(true);
            e.printStackTrace();
        } catch (AssertionError e) {

            result.sampleEnd();
            result.setResponseCode("500");
            result.setResponseMessage("NO");
            result.setSuccessful(false);
            e.printStackTrace();
        }


        return result;
    }


    public void teardownTest(JavaSamplerContext javaSamplerContext) {
        System.out.println("...........Completing the test with counting  cnt..........");

    }

    public Arguments getDefaultParameters() {
        return null;

    }
}
