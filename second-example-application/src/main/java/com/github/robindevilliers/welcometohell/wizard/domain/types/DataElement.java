package com.github.robindevilliers.welcometohell.wizard.domain.types;

import com.github.robindevilliers.welcometohell.wizard.type.Type;

public interface DataElement extends Element {

    String getData();

    Type getType();

    void setData(String data);

    void setType(Type type);

}
