/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Feature;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeatureConverter {

    UseCaseConverter useCaseConverter;
    UserConverter userConverter;

    @Autowired
    public FeatureConverter(UseCaseConverter useCaseConverter, UserConverter userConverter) {
        this.useCaseConverter = useCaseConverter;
        this.userConverter = userConverter;
    }

    /**
     * this method converts a FeatureList (Model) to a FeatureList(Domain)
     * @param featureList List of {@link Feature} obtained from DB
     * @return List of {@link FeatureDom}
     */
    public List<FeatureDom> toDomain(List<Feature> featureList) {
        List<FeatureDom> featureDomList = new ArrayList<>();
        for(Feature feature: featureList){
            List<UseCaseDom> useCaseDomList = useCaseConverter.toDomain(feature.getUseCases());
            UserDom author = userConverter.toDomain(feature.getAuthor());
            UserDom assignedTo = userConverter.toDomain(feature.getAssignedTo());
            FeatureDom featureDom = new FeatureDom(
                    feature.getIdFeature(),
                    feature.getName(),
                    feature.getIdentifier(),
                    author,
                    assignedTo,
                    feature.getObject().getProject().getIdProject(),
                    useCaseDomList
            );
            featureDomList.add(featureDom);
        }
        return featureDomList;
    }

    /**
     * This method converts an {@link Feature} (DB) to an {@link FeatureDom} (Domain)
     * @param feature {@link Feature} obtained from DB
     * @return {@link FeatureDom}
     */
    public FeatureDom toDomain(Feature feature) {
        List<UseCaseDom> useCaseDomList = useCaseConverter.toDomain(feature.getUseCases());
        UserDom author = userConverter.toDomain(feature.getAuthor());
        UserDom assignedTo = userConverter.toDomain(feature.getAssignedTo());
        if(feature.getEstimatedHours() == null){
            feature.setEstimatedHours(0.00);
        }
        if (feature.getStoryPoints()==null){
            feature.setStoryPoints(0);
        }
        FeatureDom featureDom = new FeatureDom(
                feature.getIdFeature(),
                feature.getName(),
                feature.getIdentifier(),
                feature.getDescription(),
                feature.getPriority(),
                feature.getComplexity(),
                feature.getState(),
                feature.getCost(),
                feature.getEstimatedHours(),
                feature.getStoryPoints(),
                feature.getSource(),
                feature.getScope(),
                feature.getRisk(),
                feature.getCreated(),
                feature.getLastUpdated(),
                feature.getVersion(),
                feature.getValidationMethod(),
                author,
                assignedTo,
                feature.getJustification(),
                feature.getTestCases(),
                feature.getObject().getProject().getIdProject(),
                useCaseDomList
        );
        return featureDom;
    }

    /**
     * This method converts an {@link FeatureDom} (Domain) to an {@link Feature} (DB)
     * @param featureDom {@link FeatureDom}
     * @return {@link Feature}
     */
    public Feature toModel(FeatureDom featureDom) {
        if(featureDom.getCost()==null){
            featureDom.setCost(0.0);
        }
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(featureDom.getIdFeature());
        Feature feature = new Feature();
        feature.setIdFeature(featureDom.getIdFeature());
        feature.setName(featureDom.getName());
        feature.setIdentifier(featureDom.getIdentifier());
        feature.setDescription(featureDom.getDescription());
        feature.setPriority(featureDom.getPriority());
        feature.setComplexity(featureDom.getComplexity());
        feature.setState(featureDom.getState());
        feature.setCost(featureDom.getCost());
        feature.setEstimatedHours(featureDom.getEstimatedHours());
        feature.setStoryPoints(featureDom.getStoryPoints());
        feature.setSource(featureDom.getSource());
        feature.setScope(featureDom.getScope());
        feature.setRisk(featureDom.getRisk());
        feature.setCreated(featureDom.getCreated());
        feature.setLastUpdated(featureDom.getLastUpdated());
        feature.setVersion(featureDom.getVersion());
        feature.setValidationMethod(featureDom.getValidationMethod());
        feature.setJustification(featureDom.getJustification());
        feature.setTestCases(featureDom.getTestCases());
        feature.setObject(objectEntity);
        return feature;
    }
}
