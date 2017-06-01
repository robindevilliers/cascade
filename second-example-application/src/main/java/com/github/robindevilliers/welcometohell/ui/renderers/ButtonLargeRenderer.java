package com.github.robindevilliers.welcometohell.ui.renderers;

import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.wizard.domain.ButtonLarge;

import java.io.StringWriter;
import java.util.Map;

@Component
public class ButtonLargeRenderer implements Renderer<ButtonLarge> {
    @Override
    public boolean accepts(Class<?> clz) {
        return ButtonLarge.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, ButtonLarge element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "btn btn-lg", "btn");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(velocityEngine, element, data, context);

        velocityEngine.getTemplate("views/button-large.html").merge(context, content);
    }
}