package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.RequirementType;
import com.Sergio.EasyRMT.Model.types.Requirement_Type;
import com.Sergio.EasyRMT.Service.Converter.ReqTypeConverter;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReqTypeConverterTest {
    @Test
    @DisplayName("Method toDomain receives a list of RequirementType and returns a list of ReqTypeDom")
    public void toDomain_reqTypeListProvided_ReqTypeDomListReturned(){
        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("reqType");
        requirementType.setType(Requirement_Type.SCOPE);
        ArrayList<RequirementType> listReqType = new ArrayList<>();
        listReqType.add(requirementType);

        RequirementTypeDom requirementTypeDomExpected = new RequirementTypeDom(1,"reqType", Requirement_Type.SCOPE);
        List<RequirementTypeDom> listReqTypeExpected = new ArrayList<>();
        listReqTypeExpected.add(requirementTypeDomExpected);

        ReqTypeConverter reqTypeConverter = new ReqTypeConverter();

        //TestConditions
        List<RequirementTypeDom> list = reqTypeConverter.toDomain(listReqType);
        //Test conditions
        assertNotNull(list);
        assertTrue(list.toArray().length == 1);
        assertFalse(list.isEmpty());
        assertEquals(listReqTypeExpected,list);
    }

    @Test
    @DisplayName("Method toDomain receives a RequirementType and returns a  ReqTypeDom")
    public void toDomain_reqTypeProvided_ReqTypeDomReturned(){
        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("reqType");
        requirementType.setType(Requirement_Type.SCOPE);

        RequirementTypeDom requirementTypeDomExpected = new RequirementTypeDom(1,"reqType", Requirement_Type.SCOPE);

        ReqTypeConverter reqTypeConverter = new ReqTypeConverter();

        //TestConditions
        RequirementTypeDom requirementTypeDom = reqTypeConverter.toDomain(requirementType);
        //Test conditions
        assertNotNull(requirementTypeDom);
        assertEquals(requirementTypeDomExpected,requirementTypeDom);
    }
}
