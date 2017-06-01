package com.github.robindevilliers.welcometohell.ui.renderers;

import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.ui.RenderingEngine;
import com.github.robindevilliers.welcometohell.wizard.domain.Row;

import java.io.StringWriter;
import java.util.Map;

@Component
public class RowRenderer implements Renderer<Row> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Row.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Row element, StringWriter output, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "", "bg");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/tr.html").merge(context, output);
    }
}