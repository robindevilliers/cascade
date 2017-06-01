package com.github.robindevilliers.welcometohell.ui.renderers;

import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.wizard.domain.Option;

import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

import static com.github.robindevilliers.welcometohell.wizard.type.Serialization.serialize;

@Component
public class OptionRenderer implements Renderer<Option> {
    @Override
    public boolean accepts(Class<?> clz) {
        return Option.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Option element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "", "bg");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(velocityEngine, element, data, context);

        context.put("name", element.getData());
        context.put("value", serialize(element.getType(), element.getValue()));
        context.put("checked" , element.getValue().equals(data.get(element.getData())));

        context.put("id", UUID.randomUUID().toString());

        velocityEngine.getTemplate("views/option.html").merge(context, content);
    }
}
