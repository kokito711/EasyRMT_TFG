/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class TraceDom {
    List<RequirementDom> scope;
    List<RequirementDom> engineering;
    List<RequirementDom> quality;
    List<RequirementDom> userExp;

    List<Integer> newTraces;
    int object;


    public TraceDom() {
    }

    public TraceDom(List<RequirementDom> scope, List<RequirementDom> engineering, List<RequirementDom> quality, List<RequirementDom> userExp) {
        this.scope = scope;
        this.engineering = engineering;
        this.quality = quality;
        this.userExp = userExp;
    }
}
