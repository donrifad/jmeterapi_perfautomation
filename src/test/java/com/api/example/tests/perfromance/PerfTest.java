package com.api.example.tests.perfromance;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.java.control.gui.JavaTestSamplerGui;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;

public class PerfTest {


    @Test
    public void testJmeterTest() throws IOException, InterruptedException, NotBoundException, JMeterEngineException {
        File jmeterHome = new File(System.getProperty("user.dir"));
        String slash = System.getProperty("file.separator");

        if (jmeterHome.exists()) {
            File jmeterProperties = new File(System.getProperty("user.dir") + "/jmeter.properties");
            if (jmeterProperties.exists()) {
                //JMeter Engine
                StandardJMeterEngine jmeter = new StandardJMeterEngine();

                //JMeter initialization (properties, log levels, locale, etc)
                JMeterUtils.setJMeterHome("\\");
                JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
                JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
                JMeterUtils.initLocale();

                // JMeter Test Plan, basically JOrphan HashTree
                HashTree testPlanTree = new HashTree();

                JavaSampler examplecomSampler = new JavaSampler();
                examplecomSampler.setName("Create Items");
                examplecomSampler.setClassname("com.api.example.tests.perfromance.JavaSamplerTest");
                examplecomSampler.setProperty(TestElement.TEST_CLASS, JavaSampler.class.getName());
                examplecomSampler.setProperty(TestElement.GUI_CLASS, JavaTestSamplerGui.class.getName());


                /*BackendListener backendListener = new BackendListener();
                backendListener.setName("Backend Listner");
                backendListener.setClassname("org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient");
                Arguments arguments = new Arguments();
                arguments.addArgument("influxdbMetricsSender", "org.apache.jmeter.visualizers.backend.influxdb.HttpMetricsSender", "=");
                arguments.addArgument("influxdbUrl", "http://10.133.13.16:8086/write?db=jmeter", "=");
                arguments.addArgument("application", "GATP_V14_Item", "=");
                arguments.addArgument("measurement", "jmeter", "=");
                arguments.addArgument("summaryOnly", "false", "=");
                arguments.addArgument("samplersRegex", ".*", "=");
                arguments.addArgument("testTitle", "GATP_V2_Item", "=");
                arguments.addArgument("eventTags", ".*", "=");
                arguments.addArgument("influxdbUrlNew", "http://10.133.13.16:8086/write?db=jmeter", "=");
                backendListener.setArguments(arguments);
                backendListener.setProperty(TestElement.TEST_CLASS, backendListener.getClassname());
                backendListener.setProperty(TestElement.GUI_CLASS, BackendListenerGui.class.getName());
*/
                // Loop Controller
                LoopController loopController = new LoopController();
                loopController.setLoops(1);
                loopController.setFirst(true);
                loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
                loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
                loopController.initialize();

                // Thread Group
                ThreadGroup threadGroup = new ThreadGroup();
                threadGroup.setName("Sample Thread Group");
                threadGroup.setScheduler(true);
                threadGroup.setNumThreads(5);
                threadGroup.setDuration(60);
                threadGroup.setRampUp(0);
                threadGroup.setSamplerController(loopController);
                threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());


                ConstantThroughputTimer timer = new ConstantThroughputTimer();
                timer.setProperty("throughput", "16.6");
                timer.setProperty("calcMode", 2);
                timer.setCalcMode(2);
                timer.setThroughput(10);
                timer.setEnabled(true);
                timer.setProperty(TestElement.TEST_CLASS, ConstantThroughputTimer.class.getName());
                timer.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());


                /*ConcurrencyThreadGroup threadGroup = new ConcurrencyThreadGroup();
                threadGroup.setName("Sample Thread Group");
                threadGroup.setTargetLevel("5");
                threadGroup.setRampUp("30");
                threadGroup.setSteps("5");
                threadGroup.setUnit("S");
                threadGroup.setHold("300");
                threadGroup.setSamplerController(loopController);
                threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());*/


                // Test Plan
                TestPlan testPlan = new TestPlan("Create JMeter Script From Java Code");

                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

                // Construct Test Plan from previously initialized elements
                testPlanTree.add(testPlan);
                HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
                threadGroupHashTree.add(examplecomSampler);
                threadGroupHashTree.add(timer);
                //  threadGroupHashTree.add(backendListener);


                // save generated test plan to JMeter's .jmx file format
                SaveService.saveTree(testPlanTree, new FileOutputStream(jmeterHome + "/report/jmeter_api_sample.jmx"));
                JMeter.convertSubTree(testPlanTree);


                //add Summarizer output to get test progress in stdout like:
                // summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
                Summariser summer = null;
                String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
                if (summariserName.length() > 0) {
                    summer = new Summariser(summariserName);
                }

                // Store execution results into a .jtl file, we can save file as csv also
                String reportFile = System.getProperty("user.dir") + "/report/report.jtl";
                String csvFile = System.getProperty("user.dir") + "/report/report.csv";
                ResultCollector logger = new ResultCollector(summer);
                logger.setFilename(reportFile);
                ResultCollector csvlogger = new ResultCollector(summer);
                csvlogger.setFilename(csvFile);
                testPlanTree.add(testPlanTree.getArray()[0], logger);
                testPlanTree.add(testPlanTree.getArray()[0], csvlogger);


               /* List<JMeterEngine> engines = new LinkedList();
                Properties remoteProps = new Properties();
                //set properties you want to send to remote clients here

                DistributedRunner distributedRunner = new DistributedRunner(remoteProps);

                List<String> hosts = new LinkedList();
                //add your JMeter slaves here

                hosts.add("10.133.13.37");

                distributedRunner.setStdout(System.out);
                distributedRunner.setStdErr(System.err);
                distributedRunner.init(hosts, testPlanTree);
                engines.addAll(distributedRunner.getEngines());
                distributedRunner.start();*/

                jmeter.configure(testPlanTree);
                jmeter.runTest();

                Thread.sleep(60000);

                //Thread.sleep(600000);

                // System.out.println("summer"+summer.myTotals.total.counter);


                System.out.println("Test completed. See " + jmeterHome + slash + "report.jtl file for results");
                System.out.println("JMeter .jmx script is available at " + jmeterHome + slash + "jmeter_api_sample.jmx");
                System.exit(0);

            }
        }

        System.err.println("jmeterHome property is not set or pointing to incorrect location");
        System.exit(1);

    }
}
