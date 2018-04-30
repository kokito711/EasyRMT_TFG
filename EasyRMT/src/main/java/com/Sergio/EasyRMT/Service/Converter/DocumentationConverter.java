/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Model.Documentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentationConverter {
    ProjectConverter projectConverter;

    @Autowired
    public DocumentationConverter(ProjectConverter projectConverter) {
        this.projectConverter = projectConverter;
    }

    /**
     * This method converts a {@link DocumentationDom} (Domain) to a {@link Documentation} (DB)
     * @param documentationDom {@link DocumentationDom}
     * @return {@link Documentation}
     */
    public Documentation toModel(DocumentationDom documentationDom) {
        Documentation documentation = new Documentation();
        documentation.setName(documentationDom.getName());
        documentation.setPath(documentationDom.getPath());
        documentation.setSize(documentationDom.getSize());
        documentation.setType(documentationDom.getType());
        return documentation;
    }

    /**
     * This method converts a {@link Documentation} (DB) to a {@link DocumentationDom} (Domain)
     * @param documentation {@link Documentation} obtained from DB
     * @return {@link DocumentationDom}
     */
    public  DocumentationDom toDomain(Documentation documentation){
        ProjectDom projectDom = projectConverter.toDomain(documentation.getProject());
        DocumentationDom documentDom = new DocumentationDom(
                documentation.getIdDocumentation(),
                documentation.getName(),
                documentation.getSize(),
                documentation.getType(),
                documentation.getPath(),
                documentation.getObject(),
                projectDom
        );
        return documentDom;
    }

    /**
     * this method converts a DocumentationList (Model) to a DocumentationDomList(Domain)
     * @param documentationList List of {@link Documentation} obtained from DB
     * @return List of {@link DocumentationDom}
     */
    public List<DocumentationDom> toDomain(List<Documentation> documentationList){
        List<DocumentationDom> documentationDomList = new ArrayList<>();
        for(Documentation document: documentationList){
            documentationDomList.add(toDomain(document));
        }
        return documentationDomList;
    }
}
