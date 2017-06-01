package com.github.robindevilliers.welcometohell.wizard;

import com.github.robindevilliers.welcometohell.wizard.domain.RouteMappings;
import com.github.robindevilliers.welcometohell.wizard.domain.Rule;
import com.github.robindevilliers.welcometohell.wizard.domain.View;
import com.github.robindevilliers.welcometohell.wizard.domain.Wizard;
import com.github.robindevilliers.welcometohell.wizard.domain.types.DataElement;
import com.github.robindevilliers.welcometohell.wizard.exception.SessionExpiredException;
import com.github.robindevilliers.welcometohell.wizard.type.Serialization;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.wizard.expression.Expression;
import com.github.robindevilliers.welcometohell.wizard.expression.ExpressionParser;

import javax.xml.parsers.SAXParserFactory;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WizardEngine {

    private Map<String, Wizard> cache = Collections.synchronizedMap(new HashMap<>());

    private Map<String, WizardState> database = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private WizardBuilder wizardBuilder;

    @Autowired
    private ExpressionParser expressionParser;

    @Autowired
    @Qualifier("widgetEngine")
    private VelocityEngine velocityEngine;

    public String start(String wizardId) {
        Wizard wizard = getWizard(wizardId);
        WizardState wizardState = new WizardState(wizard, wizard.getStartView());

        database.put(wizardState.getWizardSessionId(), wizardState);

        return wizardState.getWizardSessionId();
    }

    public Wizard getCurrentWizard(String wizardSessionId) {
        WizardState wizardState = database.get(wizardSessionId);
        if (wizardState == null) {
            throw new SessionExpiredException();
        }
        return wizardState.getWizard();
    }

    public View getView(String wizardSessionId, String pageId) {
        return database.get(wizardSessionId).getViewForPageId(pageId);
    }

    public String getLastPageId(String wizardSessionId) {
        return database.get(wizardSessionId).getLastPageId();
    }

    public String getLastViewExternalName(String wizardSessionId) {
        String pageId = database.get(wizardSessionId).getLastPageId();
        View view = database.get(wizardSessionId).getViewForPageId(pageId);
        return view.getExternalName();
    }


    public Map<String, Object> validateData(String wizardSessionId, String pageId, Map<String, String> input) {
        View view = this.getView(wizardSessionId, pageId);
        return input.entrySet()
                .stream()
                .map(e -> {
                    DataElement dataElement = view.findDataElement(e.getKey());
                    Object value = Serialization.deserialize(dataElement.getType(), e.getValue());

                    //TODO - validation should happen here.

                    return new AbstractMap.SimpleEntry<>(e.getKey(), value);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    public void submitData(String wizardSessionId, String pageId, Map<String, Object> input) {
        database.get(wizardSessionId).setDataOnView(pageId, input);
    }

    public Map<String, Object> getData(String wizardSessionId) {
        return database.get(wizardSessionId).buildDataImage();
    }

    public void route(String wizardSessionId, String pageId) {
        WizardState wizardState = database.get(wizardSessionId);

        Map<String, Object> data = wizardState.buildDataImage();

        View nextView = null;
        RouteMappings routeMappings = wizardState.getViewForPageId(pageId).getRouteMappings();
        for (Rule rule : routeMappings.getRules()) {

            Expression expression = expressionParser.generate(rule.getCondition());
            if (expression.matches(data)) {
                Map<String, Object> dataOnView = wizardState.getDataOnView(pageId);
                rule.getVariables().forEach(v -> {
                    StringWriter content = new StringWriter();
                    velocityEngine.evaluate(new VelocityContext(data), content, "variable-resolution", v.text);
                    dataOnView.put(v.name, content.toString());
                });

                nextView = wizardState.getWizard().findView(rule.getViewId());
            }
        }
        if (nextView == null) {
            if (routeMappings.getDefault() == null) {
                throw new RuntimeException("No routes matched and no default supplied");
            }

            Map<String, Object> dataOnView = wizardState.getDataOnView(pageId);
            routeMappings.getDefault().getVariables().forEach(v -> {
                StringWriter content = new StringWriter();
                velocityEngine.evaluate(new VelocityContext(data), content, "variable-resolution", v.text);
                dataOnView.put(v.name, content.toString());
            });
            nextView = wizardState.getWizard().findView(routeMappings.getDefault().getViewId());
        }
        wizardState.setNextView(pageId, nextView);
    }

    public void setNextView(String wizardSessionId, String pageId, String viewId) {
        //this method lets the browser set arbitrary following views from any page id.
        //This will remove all history after that page id.
        //TODO - consider how this logic is impacted by change sets.

        //TODO - should a link reset all history of state or should it add to the top?

        WizardState wizardState = database.get(wizardSessionId);
        View nextView = wizardState.getWizard().findView(viewId);

        wizardState.setNextView(pageId, nextView);
    }

    private Wizard getWizard(String name) {
        synchronized (cache) {
            if (cache.containsKey(name)) {
                return cache.get(name);
            }

            try {
                SAXParserFactory.newInstance().newSAXParser().parse(
                        WizardEngine.class.getResourceAsStream("/" + name + ".xml"),
                        wizardBuilder
                );

                cache.put(name, wizardBuilder.getWizard());

                return wizardBuilder.getWizard();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
