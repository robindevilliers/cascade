package com.github.robindevilliers.cascade.modules.reporter;

import java.io.File;

public class FileStateRendering implements StateRenderingStrategy {

    @Override
    public boolean accept(Object value) {
        return value instanceof File;
    }

    @Override
    public String render(Object value) {
        return value.toString();
    }
}
