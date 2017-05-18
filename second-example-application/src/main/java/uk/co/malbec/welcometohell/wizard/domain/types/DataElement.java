package uk.co.malbec.welcometohell.wizard.domain.types;

import uk.co.malbec.welcometohell.wizard.type.Type;

public interface DataElement extends Element {

    String getData();

    Type getType();

    void setData(String data);

    void setType(Type type);

}
