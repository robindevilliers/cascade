package com.github.robindevilliers.welcometohell.ui.renderers;


import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.wizard.domain.Text;

import java.io.StringWriter;
import java.util.Map;

@Component
public class TextRenderer implements Renderer<Text> {

    @Override
    public boolean accepts(Class<?> clz) {
        return Text.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Text text, StringWriter content, Map<String, Object> data) {
        velocityEngine.evaluate(new VelocityContext(data), content, "text-renderer", text.text);
    }
}
