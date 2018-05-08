/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class TraceDom {
    List<RequirementDom> scope;
    List<RequirementDom> engineering;
    List<RequirementDom> quality;
    List<RequirementDom> userExp;
    List<FeatureDom> features;
    List<EpicDom> epics;
    List<UseCaseDom> useCases;
    List<UserStoryDom> userStories;

    List<String> newTraces;
    int object;

    public TraceDom() {

    }

    public void init(){
        scope = new ArrayList<>();
        engineering = new ArrayList<>();
        quality = new ArrayList<>();
        userExp = new ArrayList<>();
        features = new ArrayList<>();
        epics = new ArrayList<>();
        useCases = new ArrayList<>();
        userStories = new ArrayList<>();
        newTraces = new ArrayList<>();
    }


}
