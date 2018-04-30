/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Model.Documentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentationConverter {

    @Autowired
    public DocumentationConverter() {
    }

    public Documentation toModel(DocumentationDom documentationDom) {
        Documentation documentation = new Documentation();
        documentation.setName(documentationDom.getName());
        documentation.setPath(documentationDom.getPath());
        documentation.setSize(documentationDom.getSize());
        documentation.setType(documentationDom.getType());
        return documentation;
    }
}
