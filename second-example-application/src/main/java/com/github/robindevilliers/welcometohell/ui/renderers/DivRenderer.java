package com.github.robindevilliers.welcometohell.ui.renderers;


import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.ui.RenderingEngine;
import com.github.robindevilliers.welcometohell.wizard.domain.Div;

import java.io.StringWriter;
import java.util.Map;

@Component
public class DivRenderer implements Renderer<Div> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Div.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Div element, StringWriter output, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "", "bg");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/div.html").merge(context, output);
    }
}
