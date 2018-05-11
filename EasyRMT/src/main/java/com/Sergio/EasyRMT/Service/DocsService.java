package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.Feature;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.SectPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

@Service
public class DocsService {

    private ProjectService projectService;
    private FeatureService featureService;
    private EpicService epicService;
    private UserService userService;
    private UseCaseService useCaseService;
    private TraceabilityService traceabilityService;
    private UserStoryService userStoryService;
    private RequirementService requirementService;
    private ObjectFactory objectFactory;

    @Autowired
    public DocsService(ProjectService projectService, FeatureService featureService, EpicService epicService,
                       UserService userService, UseCaseService useCaseService, TraceabilityService traceabilityService,
                       UserStoryService userStoryService, RequirementService requirementService) {
        this.projectService = projectService;
        this.featureService = featureService;
        this.epicService = epicService;
        this.userService = userService;
        this.useCaseService = useCaseService;
        this.traceabilityService = traceabilityService;
        this.userStoryService = userStoryService;
        this.requirementService = requirementService;
        objectFactory = new ObjectFactory();
    }

    public File generateDocx(ProjectDom project, String type, int objectId, Locale locale) {
        FeatureDom feature;
        EpicDom epic;
        UseCaseDom useCase;
        UserStoryDom userStory;
        RequirementDom requirement;
        File exportFile;
        switch (type){
            case "feature":
                feature = featureService.getFeature(objectId);
                exportFile = new File(feature.getIdentifier()+"_"+feature.getName()+".docx");
                generateDocument(project, feature, null, null, null, null, exportFile, locale);
                break;
            case "epic":
                epic = epicService.getEpic(objectId);
                exportFile = new File(epic.getIdentifier()+"_"+epic.getName()+".docx");
                generateDocument(project, null, epic, null, null, null, exportFile, locale);
                break;
            case "usecase":
                useCase = useCaseService.getUseCase(objectId);
                exportFile = new File(useCase.getIdentifier()+"_"+useCase.getName()+".docx");
                generateDocument(project, null, null, useCase,null,null, exportFile, locale);
                break;
            case "userstory":
                userStory = userStoryService.getUserStory(objectId);
                exportFile = new File(userStory.getIdentifier()+"_"+userStory.getName()+".docx");
                generateDocument(project,null,null,null, userStory,null, exportFile, locale);
                break;
            case "requirement":
                requirement = requirementService.getRequirement(objectId);
                exportFile = new File(requirement.getIdentifier()+"_"+requirement.getName()+".docx");
                generateDocument(project, null,null,null, null, requirement, exportFile, locale);
            default:
                throw new AccessDeniedException("Not allowed");
        }
        return exportFile;
    }

    private void generateDocument(ProjectDom project, @Nullable FeatureDom feature, @Nullable EpicDom epic,
                                  @Nullable UseCaseDom useCase, @Nullable UserStoryDom userStory,
                                  @Nullable RequirementDom requirement, File exportFile, Locale locale) {
        int object = 0;
        if(feature !=null){
            object = 1;
        }
        else if(epic!=null){
            object = 2;
        }
        else if (useCase !=null){
            object = 3;
        }
        else if (userStory != null){
            object = 4;
        }
        else if (requirement != null){
            object = 5;
        }

        Map<String,String> resourceBundle = getLanguageResource(locale);
        WordprocessingMLPackage wordPackage = null;
        try {
            wordPackage = WordprocessingMLPackage.createPackage();
            // Delete the Styles part, since it clutters up our output
            MainDocumentPart mdp = wordPackage.getMainDocumentPart();
            switch (object){
                case 1:
                    mdp.addStyledParagraphOfText("Title", feature.getIdentifier()+" "+feature.getName());
                    break;
                case 2:
                    mdp.addStyledParagraphOfText("Title", epic.getIdentifier()+" "+epic.getName());
                    break;
                case 3:
                    mdp.addStyledParagraphOfText("Title", useCase.getIdentifier()+" "+useCase.getName());
                    break;
                case 4:
                    mdp.addStyledParagraphOfText("Title", userStory.getIdentifier()+" "+userStory.getName());
                    break;
                case 5:
                    mdp.addStyledParagraphOfText("Title", requirement.getIdentifier()+" "+requirement.getName());
                    break;
            }
            wordPackage.save(exportFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Map<String, String> getLanguageResource(Locale locale) {
        Map<String,String> resourceMap = new HashMap<>();
        try {
            ResourceBundle resourceBundle;
            if(locale.getLanguage().equals("en_gb")) {
                resourceBundle=ResourceBundle.getBundle("messages_en_GB");
            }
            else {
                resourceBundle=ResourceBundle.getBundle("messages_es_ES");
            }
            for(String key : resourceBundle.keySet()){
                resourceMap.put(key,resourceBundle.getString(key));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving properties file: " + e);
        }
        return resourceMap;
    }
}
