package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.ui.RenderingEngine;
import uk.co.malbec.welcometohell.wizard.domain.Column;

import java.io.StringWriter;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.*;

@Component
public class ColumnRenderer implements Renderer<Column> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Column.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Column element, StringWriter output, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "", "bg");
        generateStyles(element, context);
        generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("th.html").merge(context, output);
    }
}