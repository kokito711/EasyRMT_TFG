package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.RequirementType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReqTypeConverter {
    public List<RequirementTypeDom> toDomain(List<RequirementType> requirementTypeList){
        List<RequirementTypeDom> requirementTypeDomList = new ArrayList<>();
        for(RequirementType reqType : requirementTypeList){
            RequirementTypeDom reqTypeDom = toDomain(reqType);
            requirementTypeDomList.add(reqTypeDom);
        }
        return  requirementTypeDomList;
    }

    public RequirementTypeDom toDomain(RequirementType requirementType){
        RequirementTypeDom reqTypeDom = new RequirementTypeDom(
                requirementType.getIdType(),
                requirementType.getName(),
                requirementType.getType()
        );
        return  reqTypeDom;
    }
}
