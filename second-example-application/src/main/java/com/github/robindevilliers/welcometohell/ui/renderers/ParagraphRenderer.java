package com.github.robindevilliers.welcometohell.ui.renderers;

import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import com.github.robindevilliers.welcometohell.ui.RenderingEngine;
import com.github.robindevilliers.welcometohell.wizard.domain.Paragraph;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class ParagraphRenderer implements Renderer<Paragraph> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Paragraph.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Paragraph element, StringWriter content, Map<String, Object> data) {

        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "", "text");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/paragraph.html").merge(context, content);
    }
}