package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.ui.RenderingEngine;
import uk.co.malbec.welcometohell.wizard.domain.Cell;
import uk.co.malbec.welcometohell.wizard.domain.Table;

import java.io.StringWriter;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateClasses;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateContent;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateStyles;

@Component
public class CellRenderer implements Renderer<Cell> {

    @Autowired
    private RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Cell.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Cell element, StringWriter output, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        generateClasses(element, context, "", "bg");
        generateStyles(element, context);
        generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);

        velocityEngine.getTemplate("views/td.html").merge(context, output);
    }
}