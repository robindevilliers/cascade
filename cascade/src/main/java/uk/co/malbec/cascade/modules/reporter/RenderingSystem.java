package uk.co.malbec.cascade.modules.reporter;

import uk.co.malbec.cascade.annotations.StateRenderingRule;
import uk.co.malbec.cascade.annotations.TransitionRenderingRule;
import uk.co.malbec.cascade.exception.CascadeException;

import java.util.ArrayList;
import java.util.List;

public class RenderingSystem {

    private List<StateRenderingStrategy> stateRenderers = new ArrayList<>();
    private List<TransitionRenderingStrategy> transitionRenderers = new ArrayList<>();

    public void add(StateRenderingStrategy stateRenderingStrategy) {
        this.stateRenderers.add(0, stateRenderingStrategy);
    }

    public void add(TransitionRenderingStrategy transitionRenderingStrategy) {
        this.transitionRenderers.add(0, transitionRenderingStrategy);
    }

    public Object copy(Object value) {
        for (TransitionRenderingStrategy renderer : this.transitionRenderers) {
            if (renderer.accept(value)) {
                return renderer.copy(value);
            }
        }
        return null;
    }


    public String renderState(Object value) {
        for (StateRenderingStrategy renderer : this.stateRenderers) {
            if (renderer.accept(value)) {
                return renderer.render(value);
            }
        }
        return null;
    }

    public String renderTransition(Object value, Object copy) {
        for (TransitionRenderingStrategy renderer : this.transitionRenderers) {
            if (renderer.accept(value)) {
                return renderer.render(value, copy);
            }
        }
        return null;
    }

    public void init(Class<?> controlClass) {
        StateRenderingRule[] stateRenderingRules = controlClass.getAnnotationsByType(StateRenderingRule.class);
        for (StateRenderingRule rule : stateRenderingRules) {
            for (Class<?> clz : rule.value()) {
                try {
                    this.stateRenderers.add(0, (StateRenderingStrategy) clz.newInstance());
                } catch (InstantiationException e) {
                    throw new CascadeException("Class specified as StateRenderingStrategy class does not have a no args constructor", e);
                } catch (IllegalAccessException e) {
                    throw new CascadeException("Class specified as StateRenderingStrategy class does not have a public constructor", e);
                } catch (ClassCastException e) {
                    throw new CascadeException("Class specified as StateRenderingStrategy class is not an instance of StateRenderingStrategy", e);
                }
            }
        }

        TransitionRenderingRule[] transitionRenderingRules = controlClass.getAnnotationsByType(TransitionRenderingRule.class);
        for (TransitionRenderingRule rule : transitionRenderingRules) {
            for (Class<?> clz : rule.value()) {
                try {
                    this.transitionRenderers.add(0, (TransitionRenderingStrategy) clz.newInstance());
                } catch (InstantiationException e) {
                    throw new CascadeException("Class specified as TransitionRenderingStrategy class does not have a no args constructor", e);
                } catch (IllegalAccessException e) {
                    throw new CascadeException("Class specified as TransitionRenderingStrategy class does not have a public constructor", e);
                } catch (ClassCastException e) {
                    throw new CascadeException("Class specified as TransitionRenderingStrategy class is not an instance of TransitionRenderingStrategy", e);
                }
            }
        }
    }
}
