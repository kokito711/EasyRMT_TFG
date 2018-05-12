package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.types.*;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class DocsService {

    private ProjectService projectService;
    private FeatureService featureService;
    private EpicService epicService;
    private UseCaseService useCaseService;
    private TraceabilityService traceabilityService;
    private UserStoryService userStoryService;
    private RequirementService requirementService;
    private final String[] table1 = {"ObjectLvl1Id","ObjectLvl1Name","ObjectLvl1Author","ObjectLvl1Assigned"};
    private final String[] table2 = {"ObjectLvl2Id","ObjectLvl2Name","ObjectLvl2Author","ObjectLvl2Assigned"};
    private final String[] table3 = {"HighLvlId","HighLvlName","HighLvlAuthor","HighLvlAssigned","HighLvlType"};
    private final String[] table4 = {"EngineerLvlId","EngineerLvlName","EngineerLvlAuthor","EngineerLvlAssigned","EngineerLvlType"};
    private final String[] table5 = {"LowLvlId","LowLvlName","LowLvlAuthor","LowLvlAssigned","LowLvlType"};

    @Autowired
    public DocsService(ProjectService projectService, FeatureService featureService, EpicService epicService,
                       UseCaseService useCaseService, TraceabilityService traceabilityService,
                       UserStoryService userStoryService, RequirementService requirementService) {
        this.projectService = projectService;
        this.featureService = featureService;
        this.epicService = epicService;
        this.useCaseService = useCaseService;
        this.traceabilityService = traceabilityService;
        this.userStoryService = userStoryService;
        this.requirementService = requirementService;
    }

    public File generateDocx(ProjectDom project, String type, int objectId, Locale locale) throws IOException, Docx4JException {
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
                                  @Nullable RequirementDom requirement, File exportFile, Locale locale) throws Docx4JException {
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
        List<String> placeholder;
        try {
            switch (object){
                case 1:
                    wordPackage = getTemplate("templates/docxFiles/featureTemplate.docx");
                    placeholder = generateCommonPlaceholder();
                    replacePlaceholder(wordPackage,feature, placeholder, project, resourceBundle);
                    break;
                case 2:
                    wordPackage = getTemplate("templates/docxFiles/epicTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    replacePlaceholder(wordPackage,epic, placeholder, project, resourceBundle);
                    break;
                case 3:
                    wordPackage = getTemplate("templates/docxFiles/useCaseTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    replacePlaceholder(wordPackage,useCase, placeholder, project, resourceBundle);
                    break;
                case 4:
                   // mdp.addStyledParagraphOfText("Title", userStory.getIdentifier()+" "+userStory.getName());
                    wordPackage = getTemplate("templates/docxFiles/epicTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    replacePlaceholder(wordPackage,userStory, placeholder, project, resourceBundle);
                    break;
                case 5:
                    //mdp.addStyledParagraphOfText("Title", requirement.getIdentifier()+" "+requirement.getName());
                    wordPackage = getTemplate("templates/docxFiles/reqTemplate.docx");
                    placeholder = generateCommonPlaceholder();
                    replacePlaceholder(wordPackage,requirement, placeholder, project, resourceBundle);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        wordPackage.save(exportFile);
    }

    private List<String> generateEpicPlaceholder() {
        List<String> placeholder = generateCommonPlaceholder();
        placeholder.add("StoryPoints");
        placeholder.add("DoD_Label");
        placeholder.add("DoD_Text");
        return placeholder;
    }

    private List<String> generateCommonPlaceholder() {
        List<String> placeholder = new ArrayList<>();
        placeholder.add("Project_Name");
        placeholder.add("state");
        placeholder.add("Object_Name");
        placeholder.add("Description_Label");
        placeholder.add("Description_Text");
        placeholder.add("Priority");
        placeholder.add("Priority_Value");
        placeholder.add("Risk");
        placeholder.add("RiskValue");
        placeholder.add("Source");
        placeholder.add("Source_Value");
        placeholder.add("Cost");
        placeholder.add("CostV");
        placeholder.add("Created");
        placeholder.add("Created_Value");
        placeholder.add("Author");
        placeholder.add("Author_Value");
        placeholder.add("Assigned");
        placeholder.add("Assigned_Value");
        placeholder.add("Complexity");
        placeholder.add("Complexity_Value");
        placeholder.add("Hours");
        placeholder.add("Hours_Value");
        placeholder.add("LastModi");
        placeholder.add("LastModi_Value");
        placeholder.add("Traceability_Label");
        placeholder.add("ObjectsLvl1_Related_Label");
        placeholder.add("ObjectsLvl2RelatedLabel");
        placeholder.add("HighLvlReqRelatedLabel");
        placeholder.add("EngineerLvlRelatedLabel");
        placeholder.add("LowLvlRelatedLabel");
        placeholder.add("TracedObjectIdLabel");
        placeholder.add("TracedObjectNameLabel");
        placeholder.add("ReqType");
        return placeholder;
    }

    private WordprocessingMLPackage getTemplate(String name) {
        WordprocessingMLPackage template = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            template = WordprocessingMLPackage.load(new FileInputStream(new File(classLoader.getResource(name).getFile())));
        } catch (Docx4JException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return template;
    }


    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    private void replacePlaceholder(WordprocessingMLPackage template, FeatureDom feature, List<String> placeholder,
                                    ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        setState(placeholder, resourceBundle, information, feature.getState());
        information.put(placeholder.get(2), feature.getIdentifier()+" "+feature.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(feature.getDescription()!= null) {
            information.put(placeholder.get(4), feature.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        setPriority(placeholder, resourceBundle, information, feature.getPriority());
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        setRisk(placeholder, resourceBundle, information, feature.getRisk());
        information.put(placeholder.get(9), resourceBundle.get("epic.tab.generalInfoTab.source"));
        if(feature.getSource()!= null) {
            information.put(placeholder.get(10), feature.getSource());
        }
        else {
            information.put(placeholder.get(10), "");
        }
        information.put(placeholder.get(11), resourceBundle.get("epic.tab.additionalInfoTab.cost"));
        information.put(placeholder.get(12), Double.toString(feature.getCost())+" UM");
        information.put(placeholder.get(13), resourceBundle.get("epic.tab.additionalInfoTab.created"));
        information.put(placeholder.get(14), feature.getCreated().toString());
        information.put(placeholder.get(15), resourceBundle.get("epic.tab.generalInfoTab.author"));
        information.put(placeholder.get(16), feature.getAuthor().getUsername());
        information.put(placeholder.get(17), resourceBundle.get("epic.tab.generalInfoTab.assignedTo"));
        information.put(placeholder.get(18), feature.getAssignedTo().getUsername());
        information.put(placeholder.get(19), resourceBundle.get("epic.tab.additionalInfoTab.complexity"));
        setComplexity(placeholder, resourceBundle, information, feature.getComplexity());
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(feature.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), feature.getLastUpdated().toString());
        information.put(placeholder.get(25), resourceBundle.get("elements.traceability"));
        information.put(placeholder.get(26), resourceBundle.get("mainMenu.management.epics.label"));
        information.put(placeholder.get(27), resourceBundle.get("epic.tab.userStories"));
        information.put(placeholder.get(28), resourceBundle.get("createProject.scopeReqs"));
        information.put(placeholder.get(29), resourceBundle.get("createProject.engineerReqs"));
        information.put(placeholder.get(30), resourceBundle.get("elements.loqLevelReq"));
        information.put(placeholder.get(31), resourceBundle.get("epicsDashboard.Table.Thead.identifier"));
        information.put(placeholder.get(32), resourceBundle.get("epicsDashboard.Table.Thead.name"));
        information.put(placeholder.get(33), resourceBundle.get("reqsDashboard.Table.Thead.reqType"));
        information.put(placeholder.get(34), Integer.toString(feature.getStoryPoints())+" pts");
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
        }
        TraceDom traceability= traceabilityService.getTraceability(feature.getIdFeature());
        List<Map<String,String>> listTable1 = generateObjectMap(traceability, "feature");
        List<Map<String,String>> listTable2 = generateObjectMap(traceability, "useCase");
        List<Map<String,String>> listTable3 = generateObjectMap(traceability, "scope");
        List<Map<String,String>> listTable4 = generateObjectMap(traceability, "engineering");
        List<Map<String,String>> listTable5 = generateObjectMap(traceability, "lowLvl");
        replaceTable(table1,listTable1,template);
        replaceTable(table2,listTable2,template);
        replaceTable(table3,listTable3,template);
        replaceTable(table4,listTable4,template);
        replaceTable(table5,listTable5,template);
    }
    private void replacePlaceholder(WordprocessingMLPackage template, EpicDom epic, List<String> placeholder,
                                    ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        setState(placeholder, resourceBundle, information, epic.getState());
        information.put(placeholder.get(2), epic.getIdentifier()+" "+epic.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(epic.getDescription()!= null) {
            information.put(placeholder.get(4), epic.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        setPriority(placeholder, resourceBundle, information, epic.getPriority());
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        setRisk(placeholder, resourceBundle, information, epic.getRisk());
        information.put(placeholder.get(9), resourceBundle.get("epic.tab.generalInfoTab.source"));
        if(epic.getSource()!= null) {
            information.put(placeholder.get(10), epic.getSource());
        }
        else {
            information.put(placeholder.get(10), "");
        }
        information.put(placeholder.get(11), resourceBundle.get("epic.tab.additionalInfoTab.cost"));
        information.put(placeholder.get(12), Double.toString(epic.getCost())+" UM");
        information.put(placeholder.get(13), resourceBundle.get("epic.tab.additionalInfoTab.created"));
        information.put(placeholder.get(14), epic.getCreated().toString());
        information.put(placeholder.get(15), resourceBundle.get("epic.tab.generalInfoTab.author"));
        information.put(placeholder.get(16), epic.getAuthor().getUsername());
        information.put(placeholder.get(17), resourceBundle.get("epic.tab.generalInfoTab.assignedTo"));
        information.put(placeholder.get(18), epic.getAssignedTo().getUsername());
        information.put(placeholder.get(19), resourceBundle.get("epic.tab.additionalInfoTab.complexity"));
        setComplexity(placeholder, resourceBundle, information, epic.getComplexity());
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(epic.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), epic.getLastUpdated().toString());
        information.put(placeholder.get(25), resourceBundle.get("elements.traceability"));
        information.put(placeholder.get(26), resourceBundle.get("mainMenu.management.epics.label"));
        information.put(placeholder.get(27), resourceBundle.get("epic.tab.userStories"));
        information.put(placeholder.get(28), resourceBundle.get("createProject.scopeReqs"));
        information.put(placeholder.get(29), resourceBundle.get("createProject.engineerReqs"));
        information.put(placeholder.get(30), resourceBundle.get("elements.loqLevelReq"));
        information.put(placeholder.get(31), resourceBundle.get("epicsDashboard.Table.Thead.identifier"));
        information.put(placeholder.get(32), resourceBundle.get("epicsDashboard.Table.Thead.name"));
        information.put(placeholder.get(33), resourceBundle.get("reqsDashboard.Table.Thead.reqType"));
        information.put(placeholder.get(34), Integer.toString(epic.getStoryPoints())+" pts");
        information.put(placeholder.get(35), resourceBundle.get("epic.tab.generalInfoTab.dod"));
        information.put(placeholder.get(36), epic.getDefinitionOfDone());
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
        }
        TraceDom traceability= traceabilityService.getTraceability(epic.getIdEpic());
        List<Map<String,String>> listTable1 = generateObjectMap(traceability, "epic");
        List<Map<String,String>> listTable2 = generateObjectMap(traceability, "userStory");
        List<Map<String,String>> listTable3 = generateObjectMap(traceability, "scope");
        List<Map<String,String>> listTable4 = generateObjectMap(traceability, "engineering");
        List<Map<String,String>> listTable5 = generateObjectMap(traceability, "lowLvl");
        replaceTable(table1,listTable1,template);
        replaceTable(table2,listTable2,template);
        replaceTable(table3,listTable3,template);
        replaceTable(table4,listTable4,template);
        replaceTable(table5,listTable5,template);
    }
    private void replacePlaceholder(WordprocessingMLPackage template, UserStoryDom userStory, List<String> placeholder,
                                     ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        setState(placeholder, resourceBundle, information, userStory.getState());
        information.put(placeholder.get(2), userStory.getIdentifier()+" "+userStory.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(userStory.getDescription()!= null) {
            information.put(placeholder.get(4), userStory.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        setPriority(placeholder, resourceBundle, information, userStory.getPriority());
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        setRisk(placeholder, resourceBundle, information, userStory.getRisk());
        information.put(placeholder.get(9), resourceBundle.get("epic.tab.generalInfoTab.source"));
        if(userStory.getSource()!= null) {
            information.put(placeholder.get(10), userStory.getSource());
        }
        else {
            information.put(placeholder.get(10), "");
        }
        information.put(placeholder.get(11), resourceBundle.get("epic.tab.additionalInfoTab.cost"));
        information.put(placeholder.get(12), Double.toString(userStory.getCost())+" UM");
        information.put(placeholder.get(13), resourceBundle.get("epic.tab.additionalInfoTab.created"));
        information.put(placeholder.get(14), userStory.getCreated().toString());
        information.put(placeholder.get(15), resourceBundle.get("epic.tab.generalInfoTab.author"));
        information.put(placeholder.get(16), userStory.getAuthor().getUsername());
        information.put(placeholder.get(17), resourceBundle.get("epic.tab.generalInfoTab.assignedTo"));
        information.put(placeholder.get(18), userStory.getAssignedTo().getUsername());
        information.put(placeholder.get(19), resourceBundle.get("epic.tab.additionalInfoTab.complexity"));
        setComplexity(placeholder, resourceBundle, information, userStory.getComplexity());
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(userStory.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), userStory.getLastUpdated().toString());
        information.put(placeholder.get(25), resourceBundle.get("elements.traceability"));
        information.put(placeholder.get(26), resourceBundle.get("mainMenu.management.epics.label"));
        information.put(placeholder.get(27), resourceBundle.get("epic.tab.userStories"));
        information.put(placeholder.get(28), resourceBundle.get("createProject.scopeReqs"));
        information.put(placeholder.get(29), resourceBundle.get("createProject.engineerReqs"));
        information.put(placeholder.get(30), resourceBundle.get("elements.loqLevelReq"));
        information.put(placeholder.get(31), resourceBundle.get("epicsDashboard.Table.Thead.identifier"));
        information.put(placeholder.get(32), resourceBundle.get("epicsDashboard.Table.Thead.name"));
        information.put(placeholder.get(33), resourceBundle.get("reqsDashboard.Table.Thead.reqType"));
        information.put(placeholder.get(34), Integer.toString(userStory.getStoryPoints())+" pts");
        information.put(placeholder.get(35), resourceBundle.get("epic.tab.generalInfoTab.dod"));
        information.put(placeholder.get(36), userStory.getDefinitionOfDone());
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
        }
        TraceDom traceability= traceabilityService.getTraceability(userStory.getIdUserStory());
        List<Map<String,String>> listTable1 = generateObjectMap(traceability, "epic");
        List<Map<String,String>> listTable2 = generateObjectMap(traceability, "userStory");
        List<Map<String,String>> listTable3 = generateObjectMap(traceability, "scope");
        List<Map<String,String>> listTable4 = generateObjectMap(traceability, "engineering");
        List<Map<String,String>> listTable5 = generateObjectMap(traceability, "lowLvl");
        replaceTable(table1,listTable1,template);
        replaceTable(table2,listTable2,template);
        replaceTable(table3,listTable3,template);
        replaceTable(table4,listTable4,template);
        replaceTable(table5,listTable5,template);
    }
    private void replacePlaceholder(WordprocessingMLPackage template, UseCaseDom useCase, List<String> placeholder,
                                    ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        setState(placeholder, resourceBundle, information, useCase.getState());
        information.put(placeholder.get(2), useCase.getIdentifier()+" "+useCase.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(useCase.getDescription()!= null) {
            information.put(placeholder.get(4), useCase.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        setPriority(placeholder, resourceBundle, information, useCase.getPriority());
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        setRisk(placeholder, resourceBundle, information, useCase.getRisk());
        information.put(placeholder.get(9), resourceBundle.get("epic.tab.generalInfoTab.source"));
        if(useCase.getSource()!= null) {
            information.put(placeholder.get(10), useCase.getSource());
        }
        else {
            information.put(placeholder.get(10), "");
        }
        information.put(placeholder.get(11), resourceBundle.get("epic.tab.additionalInfoTab.cost"));
        information.put(placeholder.get(12), Double.toString(useCase.getCost())+" UM");
        information.put(placeholder.get(13), resourceBundle.get("epic.tab.additionalInfoTab.created"));
        information.put(placeholder.get(14), useCase.getCreated().toString());
        information.put(placeholder.get(15), resourceBundle.get("epic.tab.generalInfoTab.author"));
        information.put(placeholder.get(16), useCase.getAuthor().getUsername());
        information.put(placeholder.get(17), resourceBundle.get("epic.tab.generalInfoTab.assignedTo"));
        information.put(placeholder.get(18), useCase.getAssignedTo().getUsername());
        information.put(placeholder.get(19), resourceBundle.get("epic.tab.additionalInfoTab.complexity"));
        setComplexity(placeholder, resourceBundle, information, useCase.getComplexity());
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(useCase.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), useCase.getLastUpdated().toString());
        information.put(placeholder.get(25), resourceBundle.get("elements.traceability"));
        information.put(placeholder.get(26), resourceBundle.get("mainMenu.management.epics.label"));
        information.put(placeholder.get(27), resourceBundle.get("epic.tab.userStories"));
        information.put(placeholder.get(28), resourceBundle.get("createProject.scopeReqs"));
        information.put(placeholder.get(29), resourceBundle.get("createProject.engineerReqs"));
        information.put(placeholder.get(30), resourceBundle.get("elements.loqLevelReq"));
        information.put(placeholder.get(31), resourceBundle.get("epicsDashboard.Table.Thead.identifier"));
        information.put(placeholder.get(32), resourceBundle.get("epicsDashboard.Table.Thead.name"));
        information.put(placeholder.get(33), resourceBundle.get("reqsDashboard.Table.Thead.reqType"));
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
        }
        TraceDom traceability= traceabilityService.getTraceability(useCase.getIdUseCase());
        List<Map<String,String>> listTable1 = generateObjectMap(traceability, "feature");
        List<Map<String,String>> listTable2 = generateObjectMap(traceability, "useCase");
        List<Map<String,String>> listTable3 = generateObjectMap(traceability, "scope");
        List<Map<String,String>> listTable4 = generateObjectMap(traceability, "engineering");
        List<Map<String,String>> listTable5 = generateObjectMap(traceability, "lowLvl");
        replaceTable(table1,listTable1,template);
        replaceTable(table2,listTable2,template);
        replaceTable(table3,listTable3,template);
        replaceTable(table4,listTable4,template);
        replaceTable(table5,listTable5,template);
    }
    private void replacePlaceholder(WordprocessingMLPackage template, RequirementDom requirement, List<String> placeholder,
                                    ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        setState(placeholder, resourceBundle, information, requirement.getState());
        information.put(placeholder.get(2), requirement.getIdentifier()+" "+requirement.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(requirement.getDescription()!= null) {
            information.put(placeholder.get(4), requirement.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        setPriority(placeholder, resourceBundle, information, requirement.getPriority());
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        setRisk(placeholder, resourceBundle, information, requirement.getRisk());
        information.put(placeholder.get(9), resourceBundle.get("epic.tab.generalInfoTab.source"));
        if(requirement.getSource()!= null) {
            information.put(placeholder.get(10), requirement.getSource());
        }
        else {
            information.put(placeholder.get(10), "");
        }
        information.put(placeholder.get(11), resourceBundle.get("epic.tab.additionalInfoTab.cost"));
        information.put(placeholder.get(12), Double.toString(requirement.getCost())+" UM");
        information.put(placeholder.get(13), resourceBundle.get("epic.tab.additionalInfoTab.created"));
        information.put(placeholder.get(14), requirement.getCreated().toString());
        information.put(placeholder.get(15), resourceBundle.get("epic.tab.generalInfoTab.author"));
        information.put(placeholder.get(16), requirement.getAuthor().getUsername());
        information.put(placeholder.get(17), resourceBundle.get("epic.tab.generalInfoTab.assignedTo"));
        information.put(placeholder.get(18), requirement.getAssignedTo().getUsername());
        information.put(placeholder.get(19), resourceBundle.get("epic.tab.additionalInfoTab.complexity"));
        setComplexity(placeholder, resourceBundle, information, requirement.getComplexity());
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(requirement.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), requirement.getLastUpdated().toString());
        information.put(placeholder.get(25), resourceBundle.get("elements.traceability"));
        information.put(placeholder.get(26), resourceBundle.get("mainMenu.management.epics.label"));
        information.put(placeholder.get(27), resourceBundle.get("epic.tab.userStories"));
        information.put(placeholder.get(28), resourceBundle.get("createProject.scopeReqs"));
        information.put(placeholder.get(29), resourceBundle.get("createProject.engineerReqs"));
        information.put(placeholder.get(30), resourceBundle.get("elements.loqLevelReq"));
        information.put(placeholder.get(31), resourceBundle.get("epicsDashboard.Table.Thead.identifier"));
        information.put(placeholder.get(32), resourceBundle.get("epicsDashboard.Table.Thead.name"));
        information.put(placeholder.get(33), resourceBundle.get("reqsDashboard.Table.Thead.reqType"));
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
        }
        TraceDom traceability= traceabilityService.getTraceability(requirement.getIdRequirement());
        List<Map<String,String>> listTable1 = null;
        List<Map<String,String>> listTable2 = null;
        if(project.getType().equals(ProjectType.AGILE)){
            listTable1 = generateObjectMap(traceability, "epic");
            listTable2 = generateObjectMap(traceability, "userStory");
        }
        else {
            listTable1 = generateObjectMap(traceability, "feature");
            listTable2 = generateObjectMap(traceability, "requirement");
        }
        List<Map<String,String>> listTable3 = generateObjectMap(traceability, "scope");
        List<Map<String,String>> listTable4 = generateObjectMap(traceability, "engineering");
        List<Map<String,String>> listTable5 = generateObjectMap(traceability, "lowLvl");
        replaceTable(table1,listTable1,template);
        replaceTable(table2,listTable2,template);
        replaceTable(table3,listTable3,template);
        replaceTable(table4,listTable4,template);
        replaceTable(table5,listTable5,template);
    }

    private void setState(List<String> placeholder, Map<String, String> resourceBundle, Map<String, String> information, State state) {
        switch (state) {
            case DRAFT:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.draft"));
                break;
            case WORKING:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.working"));
                break;
            case TESTING:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.testing"));
                break;
            case IMPLEMENTED:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.implemented"));
                break;
            case APPROVED:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.approved"));
                break;
            case REJECTED:
                information.put(placeholder.get(1), resourceBundle.get("objectPrintable.state.rejected"));
                break;
        }
    }

    private void setPriority(List<String> placeholder, Map<String, String> resourceBundle, Map<String, String> information, Priority priority) {
        switch (priority) {
            case VERY_LOW:
                information.put(placeholder.get(6), resourceBundle.get("objectPrintable.priority.veryLow"));
                break;
            case LOW:
                information.put(placeholder.get(6), resourceBundle.get("objectPrintable.priority.low"));
                break;
            case NORMAL:
                information.put(placeholder.get(6), "Normal");
                break;
            case HIGH:
                information.put(placeholder.get(6), resourceBundle.get("objectPrintable.priority.high"));
                break;
            case VERY_HIGH:
                information.put(placeholder.get(6), resourceBundle.get("objectPrintable.priority.veryHigh"));
                break;
            case BLOCKER:
                information.put(placeholder.get(6), resourceBundle.get("objectPrintable.priority.blocker"));
                break;
        }
    }

    private void setRisk(List<String> placeholder, Map<String, String> resourceBundle, Map<String, String> information, Risk risk) {
        switch (risk) {
            case NONE:
                information.put(placeholder.get(8), resourceBundle.get("objectPrintable.risk.none"));
                break;
            case LOW:
                information.put(placeholder.get(8), resourceBundle.get("objectPrintable.priority.low"));
                break;
            case MEDIUM:
                information.put(placeholder.get(8), resourceBundle.get("objectPrintable.risk.medium"));
                break;
            case HIGH:
                information.put(placeholder.get(8), resourceBundle.get("objectPrintable.priority.high"));
                break;
        }
    }

    private void setComplexity(List<String> placeholder, Map<String, String> resourceBundle, Map<String, String> information, Complexity complexity) {
        switch (complexity) {
            case VERY_LOW:
                information.put(placeholder.get(20), resourceBundle.get("objectPrintable.priority.veryLow"));
                break;
            case LOW:
                information.put(placeholder.get(20), resourceBundle.get("objectPrintable.priority.low"));
                break;
            case NORMAL:
                information.put(placeholder.get(20), "Normal");
                break;
            case HIGH:
                information.put(placeholder.get(20), resourceBundle.get("objectPrintable.priority.high"));
                break;
            case VERY_HIGH:
                information.put(placeholder.get(20), resourceBundle.get("objectPrintable.priority.veryHigh"));
                break;
            case BLOCKER:
                information.put(placeholder.get(20), resourceBundle.get("objectPrintable.priority.blocker"));
                break;
        }
    }

    private List<Map<String,String>> generateObjectMap(TraceDom traceability, String type) {
        List<Map<String,String>> list = new ArrayList<>();
        switch (type){
            case "epic":
                if(traceability.getEpics().size()!=0) {
                    for (EpicDom epicTraced : traceability.getEpics()){
                        Map<String,String> information = populateMap(epicTraced.getIdentifier(), epicTraced.getName(),
                                epicTraced.getAuthor(), epicTraced.getAssignedTo(),null, 1);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,1);
                }
                break;
            case "feature":
                if(traceability.getFeatures().size()!=0) {
                    for (FeatureDom featureTraced : traceability.getFeatures()){
                        Map<String,String> information = populateMap(featureTraced.getIdentifier(), featureTraced.getName(),
                                featureTraced.getAuthor(), featureTraced.getAssignedTo(),null, 1);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,1);
                }
                break;
            case "useCase":
                if(traceability.getUseCases().size()!=0) {
                    for (UseCaseDom useCaseTraced : traceability.getUseCases()) {
                        Map<String, String> information = populateMap(useCaseTraced.getIdentifier(), useCaseTraced.getName(),
                                useCaseTraced.getAuthor(), useCaseTraced.getAssignedTo(),null, 2);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,2);
                }
                break;
            case "userStory":
                if(traceability.getUserStories().size()!=0) {
                    for (UserStoryDom userStoryTraced : traceability.getUserStories()) {
                        Map<String, String> information = populateMap(userStoryTraced.getIdentifier(), userStoryTraced.getName(),
                                userStoryTraced.getAuthor(), userStoryTraced.getAssignedTo(),null, 2);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,2);
                }
                break;
            case "scope":
                if(traceability.getScope().size()!=0) {
                    for (RequirementDom requirementTraced : traceability.getScope()) {
                        RequirementTypeDom reqType = projectService.getReqType(requirementTraced.getRequirementTypeId());
                        Map<String, String> information = populateMap(requirementTraced.getIdentifier(), requirementTraced.getName(),
                                requirementTraced.getAuthor(), requirementTraced.getAssignedTo(), reqType.getName(), 3);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,3);
                }
                break;
            case "engineering":
                if(traceability.getEngineering().size()!=0) {
                    for (RequirementDom requirementTraced : traceability.getEngineering()) {
                        RequirementTypeDom reqType = projectService.getReqType(requirementTraced.getRequirementTypeId());
                        Map<String, String> information = populateMap(requirementTraced.getIdentifier(), requirementTraced.getName(),
                                requirementTraced.getAuthor(), requirementTraced.getAssignedTo(), reqType.getName(), 3);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,4);
                }
                break;
            case "lowLvl":
                if(traceability.getQuality().size()!=0) {
                    for (RequirementDom requirementTraced : traceability.getQuality()) {
                        RequirementTypeDom reqType = projectService.getReqType(requirementTraced.getRequirementTypeId());
                        Map<String, String> information = populateMap(requirementTraced.getIdentifier(), requirementTraced.getName(),
                                requirementTraced.getAuthor(), requirementTraced.getAssignedTo(), reqType.getName(), 3);
                        list.add(information);
                    }
                }
                if(traceability.getUserExp().size()!=0) {
                    for (RequirementDom requirementTraced : traceability.getUserExp()) {
                        RequirementTypeDom reqType = projectService.getReqType(requirementTraced.getRequirementTypeId());
                        Map<String, String> information = populateMap(requirementTraced.getIdentifier(), requirementTraced.getName(),
                                requirementTraced.getAuthor(), requirementTraced.getAssignedTo(), reqType.getName(), 3);
                        list.add(information);
                    }
                }
                else {
                    fillEmptyObject(list,5);
                }
                break;
        }
        return list;
    }

    private Map<String, String> populateMap(String identifier, String name, UserDom author, UserDom assignedTo,
                                            @Nullable String reqTypeName,int lvl) {
        Map<String, String> information = new HashMap<>() ;
        String[] table = selectTable(lvl);
        information.put(table[0], identifier);
        information.put(table[1], name);
        information.put(table[2], author.getUsername());
        information.put(table[3], assignedTo.getUsername());
        if(lvl == 3 || lvl == 4 || lvl == 5){
            information.put(table[4], reqTypeName);
        }
        return information;
    }

    private void fillEmptyObject(List<Map<String, String>> list, int lvl) {
        Map<String,String> information = new HashMap<>();
        String[] table = selectTable(lvl);
        information.put(table[0], "");
        information.put(table[1], "");
        information.put(table[2], "");
        information.put(table[3], "");
        if(lvl == 3 || lvl == 4 || lvl == 5){
            information.put(table[4], "");
        }
        list.add(information);
    }

    private String[] selectTable(int lvl) {
        String[] table = null;
        switch (lvl) {
            case 1:
                table = table1;
                break;
            case 2:
                table = table2;
                break;
            case 3:
                table = table3;
                break;
            case 4:
                table = table4;
                break;
            case 5:
                table = table5;
                break;
        }
        return table;
    }

    private void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd,
                              WordprocessingMLPackage template) {
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

        // 1. find the table
        Tbl tempTable = getTemplateTable(tables, placeholders[0]);
        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = (Tr) rows.get(1);

            for (Map<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }

    private Tbl getTemplateTable(List<Object> tables, String templateKey) {
        for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext();) {
            Object tbl = iterator.next();
            List<?> textElements = getAllElementFromObject(tbl, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
                    return (Tbl) tbl;
            }
        }
        return null;
    }

    private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List<?> textElements = getAllElementFromObject(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue != null)
                text.setValue(replacementValue);
        }

        reviewtable.getContent().add(workingRow);
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
