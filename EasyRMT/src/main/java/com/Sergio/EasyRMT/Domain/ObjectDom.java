package com.Sergio.EasyRMT.Domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ObjectDom {

    private int idObject;

    private ProjectDom project;

    private List<TraceDom> traceability;
}
