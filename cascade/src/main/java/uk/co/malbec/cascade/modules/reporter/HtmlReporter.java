package uk.co.malbec.cascade.modules.reporter;

import junit.framework.AssertionFailedError;
import uk.co.malbec.cascade.Scenario;
import uk.co.malbec.cascade.Scope;
import uk.co.malbec.cascade.exception.CascadeException;
import uk.co.malbec.cascade.model.Journey;
import uk.co.malbec.cascade.modules.Reporter;
import uk.co.malbec.cascade.utils.Reference;

import javax.json.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;
import static java.nio.file.Files.copy;
import static java.util.Collections.emptyMap;
import static javax.json.stream.JsonGenerator.PRETTY_PRINTING;
import static uk.co.malbec.cascade.utils.Utils.map;

public class HtmlReporter implements Reporter {

    private JsonBuilderFactory builderFactory = Json.createBuilderFactory(emptyMap());
    private JsonWriterFactory writerFactory = Json.createWriterFactory(map(PRETTY_PRINTING, true));

    private File reportsDirectory;
    private File dataDirectory;

    private JsonObjectBuilder directoryItemJson;
    private JsonArrayBuilder directoryItemsJson;

    private TestResult testResult;

    private long startTime;
    private JsonObjectBuilder directoryJson;
    private Map<String, Scope> scope;
    private List<JsonObjectBuilder> steps;

    private int index;
    private RenderingSystem renderingSystem;

    public HtmlReporter(RenderingSystem renderingSystem) {
        this.renderingSystem = renderingSystem;
    }

    @Override
    public void init(Class<?> controlClass, List<Scenario> scenarios) {
        this.directoryJson = builderFactory.createObjectBuilder();
        this.directoryItemsJson = builderFactory.createArrayBuilder();

        this.startTime = System.currentTimeMillis();

        File reportsDirectory = createDestinationDirectories();
        copyTemplateFiles(reportsDirectory);

        writeStateMachineToJson(scenarios);
    }

    private File createDestinationDirectories() {
        File testDirectory = new File("./build/reports/tests");
        if (!testDirectory.exists()) {
            if (!testDirectory.mkdirs()) {
                throw new CascadeException("Unable to create test directory for cascade reports at location ./build/reports/tests");
            }
        }

        reportsDirectory = new File(testDirectory, "cascade");
        if (reportsDirectory.exists()) {

            if (!deleteDir(reportsDirectory)) {
                throw new CascadeException("Unable to delete cascade reports directory at location ./build/reports/tests/cascade");
            }
        }

        if (!reportsDirectory.mkdir()) {
            throw new CascadeException("Unable to create cascade directory for cascade reports at location ./build/reports/tests/cascade");
        }

        dataDirectory = new File(reportsDirectory, "data");
        if (!dataDirectory.mkdir()) {
            throw new CascadeException("Unable to create cascade directory for cascade reports at location ./build/reports/tests/cascade/data");
        }
        return reportsDirectory;
    }

    private void copyTemplateFiles(File reportsDirectory) {
        copyFileFromTemplate("index.html", reportsDirectory);
        copyFileFromTemplate("bootstrap.min.css", reportsDirectory);
        copyFileFromTemplate("jquery.min.js", reportsDirectory);
        copyFileFromTemplate("bootstrap.min.js", reportsDirectory);
        copyFileFromTemplate("lodash.js", reportsDirectory);
        copyFileFromTemplate("index.js", reportsDirectory);
        copyFileFromTemplate("journey.js", reportsDirectory);
        copyFileFromTemplate("journey.html", reportsDirectory);
        copyFileFromTemplate("style.css", reportsDirectory);

        new File(reportsDirectory, "fonts").mkdir();

        copyFileFromTemplate("fonts/glyphicons-halflings-regular.eot", reportsDirectory);
        copyFileFromTemplate("fonts/glyphicons-halflings-regular.svg", reportsDirectory);
        copyFileFromTemplate("fonts/glyphicons-halflings-regular.ttf", reportsDirectory);
        copyFileFromTemplate("fonts/glyphicons-halflings-regular.woff", reportsDirectory);
        copyFileFromTemplate("fonts/glyphicons-halflings-regular.woff2", reportsDirectory);

        new File(reportsDirectory, "lib").mkdir();

        copyFileFromTemplate("lib/coordinate.js", reportsDirectory);
        copyFileFromTemplate("lib/directory.js", reportsDirectory);
        copyFileFromTemplate("lib/enum.js", reportsDirectory);
        copyFileFromTemplate("lib/leg.js", reportsDirectory);
        copyFileFromTemplate("lib/path.js", reportsDirectory);
        copyFileFromTemplate("lib/plot.js", reportsDirectory);
        copyFileFromTemplate("lib/state.js", reportsDirectory);

    }

    private void writeStateMachineToJson(List<Scenario> scenarios) {
        //TODO - you need to interrogate the When and Then annotations for replaced stateId and scenarioIds
        Map<Class<?>, Class[]> states = new HashMap<>();
        JsonArrayBuilder statesJson = builderFactory.createArrayBuilder();
        JsonArrayBuilder scenariosJson = builderFactory.createArrayBuilder();
        for (Scenario scenario : scenarios) {

            JsonObjectBuilder scenarioJson = builderFactory.createObjectBuilder();
            scenarioJson.add("name", scenario.getName());
            scenarioJson.add("state", scenario.getStateCls().getName());
            scenarioJson.add("terminator", scenario.isTerminator());
            scenarioJson.add("reEntrantTerminator", scenario.isReEntrantTerminator());
            scenariosJson.add(scenarioJson);
            if (!states.containsKey(scenario.getStateCls())) {
                states.put(scenario.getStateCls(), scenario.getSteps());
            }
        }
        directoryJson.add("scenarios", scenariosJson);

        for (Map.Entry<Class<?>, Class[]> entry : states.entrySet()) {
            JsonObjectBuilder stateJson = builderFactory.createObjectBuilder();
            stateJson.add("name", entry.getKey().getCanonicalName());

            JsonArrayBuilder precedentsJson = builderFactory.createArrayBuilder();
            for (Class<?> cls : entry.getValue()) {
                precedentsJson.add(cls.getCanonicalName());
            }

            stateJson.add("precedents", precedentsJson);
            statesJson.add(stateJson);
        }
        directoryJson.add("states", statesJson);
    }


    @Override
    public void start() {

    }

    @Override
    public void setupTest(Journey journey, Map<String, Scope> scope) {
        this.index = 0;
        this.scope = scope;
        directoryItemJson = builderFactory.createObjectBuilder();

        directoryItemJson.add("journeyId", UUID.randomUUID().toString().replaceAll("-", ""));
        directoryItemJson.add("name", journey.getName());


        this.steps = new ArrayList<>();
        for (Scenario scenario : journey.getSteps()) {
            JsonObjectBuilder stepJson = builderFactory.createObjectBuilder();
            stepJson.add("name", scenario.getName());
            this.steps.add(stepJson);
        }

        StringBuilder filter = new StringBuilder();
        filter.append("@FilterTests<br>").append("Predicate filter = and(");

        boolean comma = false;
        int index = 0;
        for (Scenario scenario : journey.getSteps()) {
            if (comma) {
                filter.append(",");
            }
            filter.append("<br>&nbsp;stepAt(");
            filter.append(index++);
            filter.append(",");
            filter.append(scenario.getCls().getCanonicalName());
            filter.append(".class)");
            comma = true;
        }

        filter.append("<br>);");
        directoryItemJson.add("filter", filter.toString());
    }

    @Override
    public void startTest(Journey journey, Reference<Object> control, Reference<List<Object>> steps) {

    }


    @Override
    public void stepBegin(Object step) {

    }

    @Override
    public void stepWhenBegin(Object step, Method whenMethod) {
        for (String key : this.scope.keySet()) {
            Scope scope = this.scope.get(key);

            if (scope.getTransitionRenderingStrategy() != null){
                scope.setCopy(scope.getTransitionRenderingStrategy().copy(scope.getValue()));
            } else {
                scope.setCopy(renderingSystem.copy(scope.getValue()));
            }
        }
    }

    @Override
    public void stepWhenInvocationException(Object step, Method whenMethod, InvocationTargetException e) {
        handleInvocationException(step, e);
    }

    @Override
    public void setWhenSuccess(Object step) {


    }

    @Override
    public void stepWhenEnd(Object step, Method whenMethod) {

    }

    @Override
    public void stepThenBegin(Object step, Method thenMethod) {
        JsonObjectBuilder scopeJson = builderFactory.createObjectBuilder();
        List<String> sortedKeys = new ArrayList<>(this.scope.keySet());
        Collections.sort(sortedKeys);

        boolean hasState = false;
        boolean hasTransition = false;
        for (String key : sortedKeys) {
            Scope scope = this.scope.get(key);
            JsonObjectBuilder memberJson = builderFactory.createObjectBuilder();

            String transition = null;
            if (scope.getValue() != null && !scope.getValue().equals(scope.getCopy())){

                if (scope.getTransitionRenderingStrategy() != null){
                    transition = scope.getTransitionRenderingStrategy().render(scope.getValue(), scope.getCopy());
                } else {
                    transition = renderingSystem.renderTransition(scope.getValue(), scope.getCopy());
                }

                if (transition != null){
                    hasTransition = true;
                    memberJson.add("transition", transition);
                }
            }

            String state;
            if (scope.getStateRenderingStrategy() != null){
                state = scope.getStateRenderingStrategy().render(scope.getValue());
            } else {
                state = renderingSystem.renderState(scope.getValue());
            }

            if (state != null){
                hasState = true;
                memberJson.add("state", state);
            }

            if (state != null || transition != null){
                scopeJson.add(key, memberJson);
            }
        }

        JsonObjectBuilder stepJson = this.steps.get(this.index);

        stepJson.add("hasState", hasState);
        stepJson.add("hasTransition", hasTransition);
        stepJson.add("scope", scopeJson);
    }

    @Override
    public void stepThenSuccess(Object step) {

    }

    @Override
    public void stepThenInvocationException(Object step, Method thenMethod, InvocationTargetException e) {
        handleInvocationException(step, e);
    }

    @Override
    public void stepThenEnd(Object step, Method thenMethod) {

    }

    @Override
    public void endStep(Object step) {
        this.index++;
    }

    @Override
    public void success(Journey journey) {
        this.testResult = TestResult.SUCCESS;
    }

    @Override
    public void tearDown(Reference<Object> control, Reference<List<Object>> steps) {

    }

    @Override
    public void handleUnknownException(RuntimeException e, Journey journey) {
        handleException(null, e);
    }

    @Override
    public void finishTest(Journey journey) {
        if (directoryItemJson == null){
            return;
        }

        if (steps != null){
            JsonArrayBuilder scenariosJson = builderFactory.createArrayBuilder();
            if (steps != null){
                for (JsonObjectBuilder step : steps) {
                    scenariosJson.add(step);
                }

            }
            directoryItemJson.add("scenarios", scenariosJson);
        }

        directoryItemJson.add("result", testResult.toString());
        directoryItemsJson.add(directoryItemJson);
    }

    @Override
    public void finish() {
        directoryJson.add("duration", System.currentTimeMillis() - startTime);
        directoryJson.add("items", directoryItemsJson.build());
        writeVariableAsFile(dataDirectory, "directoryData", directoryJson.build());

//        //TODO - temporary
//        try {
//            new ProcessBuilder("open", reportsDirectory.getAbsolutePath() + "/index.html").start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void handleInvocationException(Object step, InvocationTargetException e) {
        if (e.getTargetException() instanceof AssertionFailedError) {
            AssertionFailedError assertionFailedError = (AssertionFailedError) e.getTargetException();
            testResult = TestResult.FAILED;
            directoryItemJson.add("assertionMessage", assertionFailedError.getMessage());
            directoryItemJson.add("stackTrace", extractStackTrace(assertionFailedError));
            directoryItemJson.add("failingStep", step.getClass().getName());
        } else {
            handleException(step, e.getCause());
        }
    }

    private void handleException(Object step, Throwable e) {
        testResult = TestResult.ERROR;

        String message = e.getMessage() != null ? e.getMessage() : "null";
        directoryItemJson.add("errorMessage", message);
        directoryItemJson.add("stackTrace", extractStackTrace(e));
        if (step != null) {
            directoryItemJson.add("failingStep", step.getClass().getName());
        }
    }

    private String extractStackTrace(Throwable f) {
        StringWriter stackTrace = new StringWriter();
        f.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString().replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;");
    }


    private void copyFileFromTemplate(String fileName, File baseDirectory) {
        try {
            InputStream is = HtmlReporter.class.getResourceAsStream("/template/" + fileName);
            copy(is, Paths.get(format("%s/%s", baseDirectory.getAbsolutePath(), fileName)));
        } catch (IOException e) {
            throw new CascadeException("io error copying file " + fileName, e);
        }
    }


    private void writeVariableAsFile(File reportsDirectory, String variableName, String fileName, JsonStructure data) {
        try {
            PrintWriter printWriter = new PrintWriter(new File(reportsDirectory, fileName + ".js"));
            printWriter.print("var " + variableName + " = ");

            JsonWriter jsonWriter = writerFactory.createWriter(printWriter);
            jsonWriter.write(data);
            jsonWriter.close();
        } catch (IOException e) {
            throw new CascadeException("io error writing file " + reportsDirectory + "/" + variableName + ".js", e);
        }
    }

    private void writeVariableAsFile(File reportsDirectory, String variableName, JsonStructure data) {
        writeVariableAsFile(reportsDirectory, variableName, variableName, data);
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (String filename : dir.list()) {
                boolean success = deleteDir(new File(dir, filename));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private enum TestResult {
        SUCCESS, FAILED, ERROR
    }
}
