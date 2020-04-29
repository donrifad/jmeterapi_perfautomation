package com.api.example.tests.perfromance;

import com.api.example.util.JmeterHelper;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.Test;

import java.io.IOException;

public class SimplifiedPerfTest {

    @Test
    public void testJmeterTestWithCall() throws IOException, InterruptedException {

        StandardJMeterEngine jMeterEngine = JmeterHelper.startJmeter(System.getProperty("user.dir"));
        JavaSampler sampler = JmeterHelper.createJavaSampler("com.api.example.tests.perfromance.JavaSamplerTest", "Get_Employees");
        LoopController loopController = JmeterHelper.createLoopController(1);
        ThreadGroup threadGroup = JmeterHelper.createThreadGroup(loopController, 10, 1);
        // BackendListener grafanaListener=JmeterHelper.setGrafanaListener("QE CORE APP","Test List");
        HashTree testPlan = JmeterHelper.createTestPlanWithListeners(threadGroup, sampler, "Java Test");
        JmeterHelper.saveResult(testPlan, System.getProperty("user.dir"), System.getProperty("user.dir"), System.getProperty("user.dir"));
        JmeterHelper.runTest(jMeterEngine, testPlan);
        Thread.sleep(60000);
        // System.out.println("Test completed. See " + jmeterHome + slash + "report.jtl file for results");
        // System.out.println("JMeter .jmx script is available at " + jmeterHome + slash + "jmeter_api_sample.jmx");
        System.exit(0);
    }
}
