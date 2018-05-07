package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.RequirementType;
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
/*    @Test
    @DisplayName("Method toDomain receives a list of RequirementType and returns a list of ReqTypeDom")
    public void toDomain_reqTypeListProvided_ReqTypeDomListReturned(){
        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("reqType");
        ArrayList<RequirementType> listReqType = new ArrayList<>();
        listReqType.add(requirementType);

        RequirementTypeDom requirementTypeDomExpected = new RequirementTypeDom(1,"reqType");
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
    }*/
}
