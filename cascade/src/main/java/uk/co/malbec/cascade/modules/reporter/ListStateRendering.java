package uk.co.malbec.cascade.modules.reporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ListStateRendering implements StateRenderingStrategy {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean accept(Object value) {
        return List.class.isAssignableFrom(value.getClass());
    }

    @Override
    public String render(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "ERROR - serializing value.";
        }
    }
}
