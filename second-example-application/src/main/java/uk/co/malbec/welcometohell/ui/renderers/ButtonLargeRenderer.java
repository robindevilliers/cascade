package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.wizard.domain.ButtonLarge;

import java.io.StringWriter;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.*;

@Component
public class ButtonLargeRenderer implements Renderer<ButtonLarge> {
    @Override
    public boolean accepts(Class<?> clz) {
        return ButtonLarge.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, ButtonLarge element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "btn btn-lg", "btn");
        generateStyles(element, context);
        generateContent(velocityEngine, element, data, context);

        velocityEngine.getTemplate("views/button-large.html").merge(context, content);
    }
}