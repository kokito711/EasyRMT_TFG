package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.DocumentationDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Model.Documentation;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Service.Converter.DocumentationConverter;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class DocumentationConverterTest {
    @Mock
    ProjectConverter projectConverter;

    @BeforeEach
    public void initMocks(){
        projectConverter = mock(ProjectConverter.class);
    }


    @Test
    @DisplayName("toDomain converts a list of model entity into a list of domain entity")
    public void toDomain_ListModelProvided_ListDomainReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        Documentation documentation = mock(Documentation.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        List<Documentation> documentationList = new ArrayList<>();
        documentationList.add(documentation);

        when(documentation.getProject()).thenReturn(project);
        when(projectConverter.toDomain(project)).thenReturn(projectDom);
        when(documentation.getIdDocumentation()).thenReturn(1);
        when(documentation.getName()).thenReturn("MockDoc");
        when(documentation.getSize()).thenReturn(25.46);
        when(documentation.getType()).thenReturn("Type");
        when(documentation.getPath()).thenReturn("Path");
        when(documentation.getObject()).thenReturn(objectEntity);

        DocumentationConverter documentationConverter = createDocumentationConverter();
        List<DocumentationDom> documentationDomList = documentationConverter.toDomain(documentationList);

        assertNotNull(documentationDomList);
        assertTrue(documentationDomList.size()==1);
        assertFalse(documentationDomList.isEmpty());
        verify(documentation, times(1)).getProject();
        verify(projectConverter, times(1)).toDomain(project);
        verify(documentation, times(1)).getName();
        verify(documentation, times(1)).getSize();
        verify(documentation, times(1)).getType();
        verify(documentation, times(1)).getPath();
        verify(documentation, times(1)).getObject();
    }

    @Test
    @DisplayName("toDomain converts a model entity into a domain entity")
    public void toDomain_ModelProvided_DomainReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        Project project = mock(Project.class);
        Documentation documentation = mock(Documentation.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);

        when(documentation.getProject()).thenReturn(project);
        when(projectConverter.toDomain(project)).thenReturn(projectDom);
        when(documentation.getIdDocumentation()).thenReturn(1);
        when(documentation.getName()).thenReturn("MockDoc");
        when(documentation.getSize()).thenReturn(25.46);
        when(documentation.getType()).thenReturn("Type");
        when(documentation.getPath()).thenReturn("Path");
        when(documentation.getObject()).thenReturn(objectEntity);

        DocumentationConverter documentationConverter = createDocumentationConverter();
        DocumentationDom documentationDom = documentationConverter.toDomain(documentation);

        assertNotNull(documentation);
        verify(documentation, times(1)).getProject();
        verify(projectConverter, times(1)).toDomain(project);
        verify(documentation, times(1)).getName();
        verify(documentation, times(1)).getSize();
        verify(documentation, times(1)).getType();
        verify(documentation, times(1)).getPath();
        verify(documentation, times(1)).getObject();
    }

    @Test
    @DisplayName("toModel converts a domain entity into a model entity")
    public void toModel_DomainProvided_ModelReturned(){
        DocumentationDom documentationDom = mock(DocumentationDom.class);

        when(documentationDom.getName()).thenReturn("MockDoc");
        when(documentationDom.getSize()).thenReturn(25.46);
        when(documentationDom.getType()).thenReturn("Type");
        when(documentationDom.getPath()).thenReturn("Path");

        DocumentationConverter documentationConverter = createDocumentationConverter();
        Documentation documentation = documentationConverter.toModel(documentationDom);

        assertNotNull(documentation);
        verify(documentationDom, times(1)).getName();
        verify(documentationDom, times(1)).getSize();
        verify(documentationDom, times(1)).getType();
        verify(documentationDom, times(1)).getPath();
    }



    private DocumentationConverter createDocumentationConverter(){
        return new DocumentationConverter(projectConverter);
    }
}
