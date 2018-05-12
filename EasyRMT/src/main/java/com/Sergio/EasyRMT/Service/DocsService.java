package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.*;
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
                                  @Nullable RequirementDom requirement, File exportFile, Locale locale) throws IOException, Docx4JException {
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
/*            wordPackage = WordprocessingMLPackage.createPackage();
            // Delete the Styles part, since it clutters up our output
            MainDocumentPart mdp = wordPackage.getMainDocumentPart();*/
            switch (object){
                case 1:
                    //mdp.addStyledParagraphOfText("Title", feature.getIdentifier()+" "+feature.getName());
                    wordPackage = getTemplate("templates/docxFiles/featureTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    replacePlaceholder(wordPackage,feature, placeholder, project, resourceBundle);
                    break;
                case 2:
                    //mdp.addStyledParagraphOfText("Title", epic.getIdentifier()+" "+epic.getName());
                    wordPackage = getTemplate("templates/docxFiles/epicTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    replacePlaceholder(wordPackage,epic, placeholder, project, resourceBundle);
                    break;
                case 3:
                   // mdp.addStyledParagraphOfText("Title", useCase.getIdentifier()+" "+useCase.getName());
                    wordPackage = getTemplate("templates/docxFiles/useCaseTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    break;
                case 4:
                   // mdp.addStyledParagraphOfText("Title", userStory.getIdentifier()+" "+userStory.getName());
                    wordPackage = getTemplate("templates/docxFiles/userStoryTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    break;
                case 5:
                    //mdp.addStyledParagraphOfText("Title", requirement.getIdentifier()+" "+requirement.getName());
                    wordPackage = getTemplate("templates/docxFiles/requirementTemplate.docx");
                    placeholder = generateEpicPlaceholder();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //replaceParagraph(placeholder, toAdd, template, template.getMainDocumentPart());
        wordPackage.save(exportFile);
    }

    private List<String> generateEpicPlaceholder() {
        List<String> placeholder = new ArrayList<String>();
        generateCommonPlaceholder(placeholder);
        placeholder.add("StoryPoints");
        placeholder.add("DoD_Label");
        placeholder.add("DoD_Text");
        return placeholder;
    }

    private void generateCommonPlaceholder(List<String> placeholder) {
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

       /* for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }*/
    }

    private void replacePlaceholder(WordprocessingMLPackage template, EpicDom epic, List<String> placeholder,
                                    ProjectDom project, Map<String,String> resourceBundle) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        Map<String,String> information = new HashMap<>();
        information.put(placeholder.get(0),project.getName());
        switch (epic.getState()){
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
        information.put(placeholder.get(2), epic.getIdentifier()+" "+epic.getName());
        information.put(placeholder.get(3), resourceBundle.get("epic.tab.generalInfoTab.description"));
        if(epic.getDescription()!= null) {
            information.put(placeholder.get(4), epic.getDescription());
        }
        else {
            information.put(placeholder.get(4), "");
        }
        information.put(placeholder.get(5), resourceBundle.get("epic.tab.generalInfoTab.priority"));
        switch (epic.getPriority()){
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
                information.put(placeholder.get(6),  resourceBundle.get("objectPrintable.priority.high"));
                break;
            case VERY_HIGH:
                information.put(placeholder.get(6),  resourceBundle.get("objectPrintable.priority.veryHigh"));
                break;
            case BLOCKER:
                information.put(placeholder.get(6),  resourceBundle.get("objectPrintable.priority.blocker"));
                break;
        }
        information.put(placeholder.get(7), resourceBundle.get("epic.tab.generalInfoTab.risk"));
        switch (epic.getRisk()){
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
                information.put(placeholder.get(8),  resourceBundle.get("objectPrintable.priority.high"));
                break;
        }
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
        switch (epic.getComplexity()){
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
                information.put(placeholder.get(20),  resourceBundle.get("objectPrintable.priority.high"));
                break;
            case VERY_HIGH:
                information.put(placeholder.get(20),  resourceBundle.get("objectPrintable.priority.veryHigh"));
                break;
            case BLOCKER:
                information.put(placeholder.get(20),  resourceBundle.get("objectPrintable.priority.blocker"));
                break;
        }
        information.put(placeholder.get(21), resourceBundle.get("epic.tab.additionalInfoTab.estHours"));
        information.put(placeholder.get(22), Double.toString(epic.getEstimatedHours())+" "+resourceBundle.get("objectPrintable.hours"));
        information.put(placeholder.get(23), resourceBundle.get("epic.tab.additionalInfoTab.lastUpdated"));
        information.put(placeholder.get(24), epic.getLastUpdated().toString());
        information.put(placeholder.get(25), Integer.toString(epic.getStoryPoints())+" pts");
        information.put(placeholder.get(26), resourceBundle.get("epic.tab.generalInfoTab.dod"));
        information.put(placeholder.get(27), epic.getDefinitionOfDone());
        for (Object text : texts) {
            Text textElement = (Text) text;
            /*if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }*/
            if (information.containsKey(textElement.getValue())){
                textElement.setValue(information.get(textElement.getValue()));
            }
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
