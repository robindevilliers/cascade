package com.github.robindevilliers.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.ui.RenderingEngine;
import com.github.robindevilliers.welcometohell.wizard.domain.Table;

import java.io.StringWriter;
import java.util.Map;

import static com.github.robindevilliers.welcometohell.ui.RendererUtilities.generateClasses;
import static com.github.robindevilliers.welcometohell.ui.RendererUtilities.generateContent;
import static com.github.robindevilliers.welcometohell.ui.RendererUtilities.generateStyles;

@Component
public class TableRenderer implements Renderer<Table> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Table.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Table element, StringWriter output, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "", "bg");
        generateStyles(element, context);
        generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/table.html").merge(context, output);
    }
}
