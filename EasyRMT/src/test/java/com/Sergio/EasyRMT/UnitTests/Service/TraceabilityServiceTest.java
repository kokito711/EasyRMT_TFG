package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Model.types.Requirement_Type;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TraceabilityServiceTest {

    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private RequirementService requirementService;
    @Mock
    private ReqTypeRepository reqTypeRepository;
    @Mock
    private FeatureService featureService;
    @Mock
    private EpicService epicService;
    @Mock
    private UseCaseService useCaseService;
    @Mock
    private UserStoryService userStoryService;

    @BeforeEach
    public void init(){
        objectRepository = mock(ObjectRepository.class);
        requirementService = mock(RequirementService.class);
        reqTypeRepository = mock(ReqTypeRepository.class);
        featureService = mock(FeatureService.class);
        epicService = mock(EpicService.class);
        useCaseService = mock(UseCaseService.class);
        userStoryService = mock(UserStoryService.class);
    }

    @Test
    @DisplayName("Get traceability returns traceability of an object")
    public void getTraceability_ObjectIdProvided_TraceabilityObtained(){
        FeatureDom feature = new FeatureDom();
        EpicDom epic = new EpicDom();
        UserStoryDom userstory = new UserStoryDom();
        UseCaseDom useCase = new UseCaseDom();
        RequirementDom requirementDom =mock(RequirementDom.class);
        RequirementType requirementType = mock(RequirementType.class);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(1);
        List<ObjectEntity> tracedDomList = new ArrayList<>();
        generateTracedList(tracedDomList);
        objectEntity.setTraced(tracedDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity);
        when(requirementService.exists(2)).thenReturn(false);
        when(featureService.exists(2)).thenReturn(true);
        when(featureService.getFeature(2)).thenReturn(feature);
        when(requirementService.exists(3)).thenReturn(false);
        when(featureService.exists(3)).thenReturn(false);
        when(epicService.exists(3)).thenReturn(true);
        when(epicService.getEpic(3)).thenReturn(epic);
        when(requirementService.exists(4)).thenReturn(false);
        when(featureService.exists(4)).thenReturn(false);
        when(epicService.exists(4)).thenReturn(false);
        when(userStoryService.exists(4)).thenReturn(true);
        when(userStoryService.getUserStory(4)).thenReturn(userstory);
        when(requirementService.exists(5)).thenReturn(false);
        when(featureService.exists(5)).thenReturn(false);
        when(epicService.exists(5)).thenReturn(false);
        when(userStoryService.exists(5)).thenReturn(false);
        when(useCaseService.exists(5)).thenReturn(true);
        when(useCaseService.getUseCase(5)).thenReturn(useCase);
        when(requirementService.exists(6)).thenReturn(true);
        when(requirementService.exists(7)).thenReturn(true);
        when(requirementService.exists(8)).thenReturn(true);
        when(requirementService.exists(9)).thenReturn(true);
        when(requirementService.getRequirement(anyInt())).thenReturn(requirementDom);
        when(reqTypeRepository.findOne(anyInt())).thenReturn(requirementType);
        doReturn(1).when(requirementDom).getRequirementTypeId();
        doReturn(2).when(requirementDom).getRequirementTypeId();
        doReturn(3).when(requirementDom).getRequirementTypeId();
        doReturn(4).when(requirementDom).getRequirementTypeId();
        when(reqTypeRepository.findOne(anyInt())).thenReturn(requirementType);
        doReturn(Requirement_Type.SCOPE).doReturn(Requirement_Type.ENGINEERING).doReturn(Requirement_Type.USER_EXP).doReturn(Requirement_Type.QA).when(requirementType).getType();

        TraceabilityService traceabilityService = createTraceabilityService();

        TraceDom expected = new TraceDom();
        expected.init();
        expected.getFeatures().add(feature);
        expected.getEpics().add(epic);
        expected.getUseCases().add(useCase);
        expected.getUserStories().add(userstory);
        expected.getScope().add(requirementDom);
        expected.getEngineering().add(requirementDom);
        expected.getUserExp().add(requirementDom);
        expected.getQuality().add(requirementDom);

        TraceDom obtained = traceabilityService.getTraceability(1);

        assertEquals(obtained, expected);
        assertNotNull(obtained);
        verify(objectRepository,times(1)).findOne(1);
        verify(requirementService, times(8)).exists(anyInt());
        verify(featureService, times(4)).exists(anyInt());
        verify(epicService, times(3)).exists(anyInt());
        verify(userStoryService, times(2)).exists(anyInt());
        verify(useCaseService, times(1)).exists(anyInt());
        verify(featureService, times(1)).getFeature(2);
        verify(epicService, times(1)).getEpic(3);
        verify(userStoryService, times(1)).getUserStory(4);
        verify(useCaseService, times(1)).getUseCase(5);
        verify(requirementService, times(4)).getRequirement(anyInt());
        verify(reqTypeRepository, times(4)).findOne(anyInt());
    }

    @Test
    @DisplayName("getNotTracedReqs returns a list with no traced reqs")
    public void getNotTracedReqs_ProjectIdAndObjectIdProvided_NotTracedReqsListProvided(){
        List<RequirementDom> requirementDomList = new ArrayList<>();
        RequirementDom requirementDom = mock(RequirementDom.class);
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        requirementDomList.add(requirementDom);
        when(requirementService.getRequirements(1)).thenReturn(requirementDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(requirementDom.getIdRequirement()).thenReturn(9);
        when(objectRepository.findOne(9)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(traced.contains(objectEntity2)).thenReturn(false);

        TraceabilityService traceabilityService = createTraceabilityService();
        List<RequirementDom> expected = traceabilityService.getNotTracedReqs(1,1);

        assertNotNull(expected);
        assertEquals(1, expected.size());
        assertEquals(expected, requirementDomList);
        verify(requirementService,times(1)).getRequirements(1);
        verify(objectRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("getNotTracedFeatures returns a list with no traced features")
    public void getNotTracedFeatures_ProjectIdAndObjectIdProvided_NotTracedFeaturesListProvided(){
        List<FeatureDom> featureDomList = new ArrayList<>();
        FeatureDom featureDom = mock(FeatureDom.class);
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        featureDomList.add(featureDom);
        when(featureService.getFeatures(1)).thenReturn(featureDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(featureDom.getIdFeature()).thenReturn(9);
        when(objectRepository.findOne(9)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(traced.contains(objectEntity2)).thenReturn(false);

        TraceabilityService traceabilityService = createTraceabilityService();
        List<FeatureDom> expected = traceabilityService.getNotTracedFeatures(1,1);

        assertNotNull(expected);
        assertEquals(1, expected.size());
        assertEquals(expected, featureDomList);
        verify(featureService,times(1)).getFeatures(1);
        verify(objectRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("getNotTracedEpics returns a list with no traced epics")
    public void getNotTracedEpics_ProjectIdAndObjectIdProvided_NotTracedEpicsListProvided(){
        List<EpicDom> epicDomList = new ArrayList<>();
        EpicDom epicDom = mock(EpicDom.class);
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        epicDomList.add(epicDom);
        when(epicService.getEpics(1)).thenReturn(epicDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(epicDom.getIdEpic()).thenReturn(9);
        when(objectRepository.findOne(9)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(traced.contains(objectEntity2)).thenReturn(false);

        TraceabilityService traceabilityService = createTraceabilityService();
        List<EpicDom> expected = traceabilityService.getNotTracedEpics(1,1);

        assertNotNull(expected);
        assertEquals(1, expected.size());
        assertEquals(expected, epicDomList);
        verify(epicService,times(1)).getEpics(1);
        verify(objectRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("getNotTracedUseCases returns a list with no traced usecases")
    public void getNotTracedUseCases_ProjectIdAndObjectIdProvided_NotTracedUseCasesListProvided(){
        List<UseCaseDom> useCaseDomList = new ArrayList<>();
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        useCaseDomList.add(useCaseDom);
        when(useCaseService.getByProjectID(1)).thenReturn(useCaseDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(useCaseDom.getIdUseCase()).thenReturn(9);
        when(objectRepository.findOne(9)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(traced.contains(objectEntity2)).thenReturn(false);

        TraceabilityService traceabilityService = createTraceabilityService();
        List<UseCaseDom> expected = traceabilityService.getNotTracedUseCases(1,1);

        assertNotNull(expected);
        assertEquals(1, expected.size());
        assertEquals(expected, useCaseDomList);
        verify(useCaseService,times(1)).getByProjectID(1);
        verify(objectRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("getNotTracedUserStories returns a list with no traced User stories")
    public void getNotTracedUserStories_ProjectIdAndObjectIdProvided_NotTracedUserStoriesListProvided(){
        List<UserStoryDom> userStoryDomList = new ArrayList<>();
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        userStoryDomList.add(userStoryDom);
        when(userStoryService.getByProjectID(1)).thenReturn(userStoryDomList);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(userStoryDom.getIdUserStory()).thenReturn(9);
        when(objectRepository.findOne(9)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(traced.contains(objectEntity2)).thenReturn(false);

        TraceabilityService traceabilityService = createTraceabilityService();
        List<UserStoryDom> expected = traceabilityService.getNotTracedUserStories(1,1);

        assertNotNull(expected);
        assertEquals(1, expected.size());
        assertEquals(expected, userStoryDomList);
        verify(userStoryService,times(1)).getByProjectID(1);
        verify(objectRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("Save relationship persist a relationship and returns true")
    public void saveRelationship_Object1IdObject2IdProvided_RelationshipSaved_TrueReturned(){
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(objectRepository.findOne(2)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(objectEntity2.getTraced()).thenReturn(traced);
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity1);

        TraceabilityService traceabilityService = createTraceabilityService();
        assertTrue(traceabilityService.saveRelationship(1,2));
        verify(objectRepository, times(2)).findOne(anyInt());
        verify(objectRepository, times(2)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("Save relationship throws an exception and returns false")
    public void saveRelationship_Object1IdObject2IdProvided_RelationshipThrowsException_FalseReturned(){
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = mock(List.class);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(objectRepository.findOne(2)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(objectEntity2.getTraced()).thenReturn(traced);
        when(objectRepository.save(any(ObjectEntity.class))).thenThrow(Exception.class);

        TraceabilityService traceabilityService = createTraceabilityService();
        assertFalse(traceabilityService.saveRelationship(1,2));
        verify(objectRepository, times(2)).findOne(anyInt());
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("Delete relationship deletes a relationship and returns true")
    public void deleteRelationship_Object1IdObject2IdProvided_RelationshipDeleted_TrueReturned(){
        ObjectEntity objectEntity1 = mock(ObjectEntity.class);
        ObjectEntity objectEntity2 = mock(ObjectEntity.class);
        List<ObjectEntity> traced = new ArrayList<>();
        List<ObjectEntity> traced2 = new ArrayList<>();
        traced.add(objectEntity2);
        traced2.add(objectEntity1);
        when(objectRepository.exists(1)).thenReturn(true);
        when(objectRepository.exists(2)).thenReturn(true);
        when(objectRepository.findOne(1)).thenReturn(objectEntity1);
        when(objectRepository.findOne(2)).thenReturn(objectEntity2);
        when(objectEntity1.getTraced()).thenReturn(traced);
        when(objectEntity2.getTraced()).thenReturn(traced2);
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity1);

        TraceabilityService traceabilityService = createTraceabilityService();
        assertTrue(traceabilityService.deleteRelatonship(1,2));
        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(2)).findOne(anyInt());
        verify(objectRepository, times(2)).save(any(ObjectEntity.class));
    }

    @Test
    @DisplayName("Delete relationship fails to delete a relationship when any object does not exists")
    public void deleteRelationship_Object1IdObject2IdProvided_FalseReturned(){
        when(objectRepository.exists(1)).thenReturn(false);
        when(objectRepository.exists(2)).thenReturn(true);

        TraceabilityService traceabilityService = createTraceabilityService();
        assertFalse(traceabilityService.deleteRelatonship(1,2));
        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).findOne(anyInt());
        verify(objectRepository, times(0)).save(any(ObjectEntity.class));
    }

    private TraceabilityService createTraceabilityService(){
        return new TraceabilityService(objectRepository,requirementService, reqTypeRepository,
                featureService,epicService,useCaseService,userStoryService);
    }

    private void generateTracedList(List<ObjectEntity> list){
        for (int i = 2; i<10; i++){
            ObjectEntity objectEntity = new ObjectEntity();
            objectEntity.setIdobject(i);
            list.add(objectEntity);
        }
    }

}
