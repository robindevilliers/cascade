package uk.co.malbec.welcometohell.ui;

import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;

public interface Renderer<T> {

    boolean accepts(Class<?> clz);

    void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, T element, StringWriter content, Map<String, Object> data);

}
