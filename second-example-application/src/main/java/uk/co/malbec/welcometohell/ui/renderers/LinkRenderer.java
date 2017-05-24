package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.wizard.domain.Link;

import java.io.StringWriter;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateClasses;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateContent;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateStyles;

@Component
public class LinkRenderer implements Renderer<Link> {
    @Override
    public boolean accepts(Class<?> clz) {
        return Link.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Link element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "", "text");
        generateStyles(element, context);
        generateContent(velocityEngine, element, data, context);

        context.put("pageId", pageId);
        context.put("viewId", element.getView());

        velocityEngine.getTemplate("link.html").merge(context, content);
    }
}