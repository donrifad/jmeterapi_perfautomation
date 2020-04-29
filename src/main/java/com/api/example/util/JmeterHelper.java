package com.api.example.util;

import com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup;
import com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroupGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.control.gui.TransactionControllerGui;
import org.apache.jmeter.engine.DistributedRunner;
import org.apache.jmeter.engine.JMeterEngine;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.java.control.gui.JavaTestSamplerGui;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jmeter.visualizers.backend.BackendListenerGui;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class JmeterHelper {

    public JmeterHelper() {
    }

    public static StandardJMeterEngine startJmeter() {
        File jmeterHome = new File(System.getProperty("user.dir") + "/src/main/java/com/syscolab/qe/core/performance/");
        String slash = System.getProperty("file.separator");
        File jmeterProperties = new File(jmeterHome + "/jmeter.properties");
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        JMeterUtils.setJMeterHome(System.getProperty("user.dir") + "/src/main/java/com/syscolab/qe/core/performance");
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();
        return jmeter;
    }

    public static StandardJMeterEngine startJmeter(String jmeterHomePath) {
        File jmeterHome = new File(jmeterHomePath);
        File jmeterProperties = new File(jmeterHome + "/jmeter.properties");
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        JMeterUtils.setJMeterHome(jmeterHomePath);
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();
        return jmeter;
    }

    public static BackendListener setGrafanaListener(String applicationName, String testTitle) {
        BackendListener backendListener = new BackendListener();
        backendListener.setName("Backend Listner");
        backendListener.setClassname("org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient");
        Arguments arguments = new Arguments();
        arguments.addArgument("influxdbMetricsSender", "org.apache.jmeter.visualizers.backend.influxdb.HttpMetricsSender", "=");
        arguments.addArgument("influxdbUrl", "http://10.133.13.16:8086/write?db=jmeter", "=");
        arguments.addArgument("application", applicationName, "=");
        arguments.addArgument("measurement", "jmeter", "=");
        arguments.addArgument("summaryOnly", "false", "=");
        arguments.addArgument("samplersRegex", ".*", "=");
        arguments.addArgument("testTitle", testTitle, "=");
        arguments.addArgument("eventTags", ".*", "=");
        arguments.addArgument("influxdbUrlNew", "http://10.133.13.16:8086/write?db=jmeter", "=");
        backendListener.setArguments(arguments);
        backendListener.setProperty("TestElement.test_class", backendListener.getClassname());
        backendListener.setProperty("TestElement.gui_class", BackendListenerGui.class.getName());
        return backendListener;
    }

    public static BackendListener setQperfListener(String projectBuild, String projectBuildVersion) {
        BackendListener qperfBackEndListener = new BackendListener();
        qperfBackEndListener.setName("Backend Listener QPerf");
        qperfBackEndListener.setClassname("com.syscolab.qperf.core.reporting.SyscoLabReporting");
        Arguments perfArguments = new Arguments();
        perfArguments.addArgument("qperf_api", "http://syscoqcenter.sysco.com:3010", "=");
        perfArguments.addArgument("project_build", projectBuild, "=");
        perfArguments.addArgument("project_build_version", projectBuildVersion, "=");
        qperfBackEndListener.setArguments(perfArguments);
        qperfBackEndListener.setProperty("TestElement.test_class", qperfBackEndListener.getClassname());
        qperfBackEndListener.setProperty("TestElement.gui_class", BackendListenerGui.class.getName());
        return qperfBackEndListener;
    }

    public static void startDistributeJMeterRunner(String host, HashTree testPlanTree) {
        List<JMeterEngine> engines = new LinkedList();
        Properties remoteProps = new Properties();
        DistributedRunner distributedRunner = new DistributedRunner(remoteProps);
        List<String> hosts = new LinkedList();
        hosts.add(host);
        distributedRunner.setStdout(System.out);
        distributedRunner.setStdErr(System.err);
        distributedRunner.init(hosts, testPlanTree);
        engines.addAll(distributedRunner.getEngines());
        distributedRunner.start();
    }

    public static JavaSampler createJavaSampler(String className) {
        JavaSampler examplecomSampler = new JavaSampler();
        examplecomSampler.setName("Java Request");
        examplecomSampler.setClassname(className);
        examplecomSampler.setProperty("TestElement.test_class", JavaSampler.class.getName());
        examplecomSampler.setProperty("TestElement.gui_class", JavaTestSamplerGui.class.getName());
        return examplecomSampler;
    }

    public static JavaSampler createJavaSampler(String className, String requestName) {
        JavaSampler examplecomSampler = new JavaSampler();
        examplecomSampler.setName(requestName);
        examplecomSampler.setClassname(className);
        examplecomSampler.setProperty("TestElement.test_class", JavaSampler.class.getName());
        examplecomSampler.setProperty("TestElement.gui_class", JavaTestSamplerGui.class.getName());
        return examplecomSampler;
    }

    public static ConstantThroughputTimer ConstantThroughputTimer(int throughput, int calcMode) {
        ConstantThroughputTimer timer = new ConstantThroughputTimer();
        timer.setProperty("throughput", throughput);
        timer.setProperty("calcMode", calcMode);
        timer.setCalcMode(calcMode);
        timer.setThroughput((double) throughput);
        timer.setEnabled(true);
        timer.setProperty("TestElement.test_class", ConstantThroughputTimer.class.getName());
        timer.setProperty("TestElement.gui_class", TestBeanGUI.class.getName());
        return timer;
    }

    public static ConcurrencyThreadGroup createConcurrencyThreadGroup(String name, String targetConcurrency, String rampUpTime, String rampUpStep, String timeUnit, String holdTargetTime) {
        ConcurrencyThreadGroup concurrencyThreadGroup = new ConcurrencyThreadGroup();
        concurrencyThreadGroup.setName(name);
        concurrencyThreadGroup.setTargetLevel(targetConcurrency);
        concurrencyThreadGroup.setRampUp(rampUpTime);
        concurrencyThreadGroup.setSteps(rampUpStep);
        concurrencyThreadGroup.setUnit(timeUnit);
        concurrencyThreadGroup.setHold(holdTargetTime);
        concurrencyThreadGroup.setEnabled(true);
        concurrencyThreadGroup.setProperty("TestElement.test_class", ConcurrencyThreadGroup.class.getName());
        concurrencyThreadGroup.setProperty("TestElement.gui_class", ConcurrencyThreadGroupGui.class.getName());
        return concurrencyThreadGroup;
    }

    public static LoopController createLoopController(int loops) {
        LoopController loopController = new LoopController();
        loopController.setLoops(loops);
        loopController.setFirst(true);
        loopController.setProperty("TestElement.test_class", LoopController.class.getName());
        loopController.setProperty("TestElement.gui_class", LoopControlPanel.class.getName());
        loopController.initialize();
        return loopController;
    }

    public static ThreadGroup createThreadGroup(LoopController loopController, int noOfThreads, int rampUp) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Sample Thread Group");
        threadGroup.setNumThreads(noOfThreads);
        threadGroup.setRampUp(rampUp);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty("TestElement.test_class", ThreadGroup.class.getName());
        threadGroup.setProperty("TestElement.gui_class", ThreadGroupGui.class.getName());
        return threadGroup;
    }

    public static ThreadGroup createThreadGroup(LoopController loopController, int noOfThreads, int rampUp, int duration) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Sample Thread Group");
        threadGroup.setNumThreads(noOfThreads);
        threadGroup.setRampUp(rampUp);
        threadGroup.setDuration((long) duration);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty("TestElement.test_class", ThreadGroup.class.getName());
        threadGroup.setProperty("TestElement.gui_class", ThreadGroupGui.class.getName());
        return threadGroup;
    }

    public static TransactionController createTransactionController(String name) {
        TransactionController transactionController = new TransactionController();
        transactionController.setGenerateParentSample(true);
        transactionController.isEnabled();
        transactionController.setName(name);
        transactionController.setProperty("TestElement.test_class", TransactionController.class.getName());
        transactionController.setProperty("TestElement.gui_class", TransactionControllerGui.class.getName());
        return transactionController;
    }

    public static HashTree createTestPlan(ThreadGroup threadGroup, JavaSampler sampler, String planName) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }

    public static TestPlan createTestPlan(String name) {
        TestPlan testPlan = new TestPlan(name);
        testPlan.setEnabled(true);
        testPlan.setSerialized(false);
        testPlan.setFunctionalMode(false);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        return testPlan;
    }

    public static HashTree createTestPlanWithListeners(ThreadGroup threadGroup, JavaSampler sampler, String planName, BackendListener grafanaListener, BackendListener qperfListener) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(grafanaListener);
        threadGroupHashTree.add(qperfListener);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }


    public static HashTree createTestPlanWithListeners(ThreadGroup threadGroup, JavaSampler sampler, String planName) {
        HashTree testPlanTree = new HashTree();
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setProperty("TestElement.test_class", TestPlan.class.getName());
        testPlan.setProperty("TestElement.gui_class", TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) (new ArgumentsPanel()).createTestElement());
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(sampler);
        return testPlanTree;
    }

    public static void saveResult(HashTree testPlanTree, String jmeterHome, String jtlPath, String csvPath) throws IOException {
        SaveService.saveTree(testPlanTree, new FileOutputStream(jmeterHome + "/report/jmeter_api_sample.jmx"));
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        String reportFile = jtlPath + "/report/report.jtl";
        String csvFile = csvPath + "/report/report.csv";
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(reportFile);
        ResultCollector csvlogger = new ResultCollector(summer);
        csvlogger.setFilename(csvFile);
        testPlanTree.add(testPlanTree.getArray()[0], logger);
        testPlanTree.add(testPlanTree.getArray()[0], csvlogger);
    }

    public static void runTest(StandardJMeterEngine jMeterEngine, HashTree testPlanTree) {
        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
    }
}
