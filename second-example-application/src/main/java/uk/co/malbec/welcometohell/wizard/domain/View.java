package uk.co.malbec.welcometohell.wizard.domain;


import uk.co.malbec.welcometohell.wizard.ElementComposer;
import uk.co.malbec.welcometohell.wizard.domain.types.DataElement;
import uk.co.malbec.welcometohell.wizard.domain.types.Element;

import java.util.List;

public class View extends ElementComposer {

    private String id;

    private String title;

    private String rem;

    private String em;

    private String externalName;

    private RouteMappings routeMappings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRem() {
        return rem;
    }

    private String getEm() {
        return em;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public RouteMappings getRouteMappings() {
        return routeMappings;
    }

    public void setRouteMappings(RouteMappings routeMappings) {
        this.routeMappings = routeMappings;
    }


    public DataElement findDataElement(String key) {
        return traverseElements(key, getElements());
    }

    private DataElement traverseElements(String key, List<Element> elements) {
        for (Element el : elements) {
            if (el instanceof DataElement) {
                DataElement dataElement = (DataElement) el;
                if (dataElement.getData().equals(key)) {
                    return dataElement;
                }
            }

            if (el instanceof ElementComposer) {
                DataElement dataElement = traverseElements(key, ((ElementComposer) el).getElements());
                if (dataElement != null) {
                    return dataElement;
                }
            }


        }
        return null;
    }


}
