package uk.co.malbec.welcometohell.ui;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.wizard.ElementComposer;
import uk.co.malbec.welcometohell.wizard.domain.types.Element;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RenderingEngine implements ApplicationContextAware {

    @Autowired
    @Qualifier("widgetEngine")
    private VelocityEngine velocityEngine;

    private List<Renderer> renderers;

    public void interpretElement(String wizardSessionId, String pageId, Element element, StringWriter content, Map<String, Object> data) {
        for (Renderer renderer : renderers) {
            if (renderer.accepts(element.getClass())) {
                renderer.render(velocityEngine, wizardSessionId, pageId, element, content, data);
                return;
            }
        }
    }


    public String renderElementComposer(String wizardSessionId, String pageId, ElementComposer elementComposer, Map<String, Object> data) {
        StringWriter content = new StringWriter();
        for (Element element : elementComposer.getElements()) {
            interpretElement(wizardSessionId, pageId, element, content, data);
        }
        return content.toString();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Renderer> renderers = applicationContext.getBeansOfType(Renderer.class);
        this.renderers = new ArrayList<>(renderers.values());
    }


}
