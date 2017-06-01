package com.github.robindevilliers.welcometohell.ui.renderers;

import com.github.robindevilliers.welcometohell.ui.Renderer;
import com.github.robindevilliers.welcometohell.ui.RendererUtilities;
import com.github.robindevilliers.welcometohell.wizard.domain.Question;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class QuestionRenderer  implements Renderer<Question> {
    @Override
    public boolean accepts(Class<?> clz) {
        return Question.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Question element, StringWriter content, Map<String, Object> data) {
        VelocityContext context = new VelocityContext();

        RendererUtilities.generateClasses(element, context, "", "text");
        RendererUtilities.generateStyles(element, context);
        RendererUtilities.generateContent(velocityEngine, element, data, context);

        velocityEngine.getTemplate("views/question.html").merge(context, content);
    }

}