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
            RequirementTypeDom reqTypeDom = new RequirementTypeDom(
                    reqType.getIdType(),
                    reqType.getName()
            );
            requirementTypeDomList.add(reqTypeDom);
        }
        return  requirementTypeDomList;
    }
}
