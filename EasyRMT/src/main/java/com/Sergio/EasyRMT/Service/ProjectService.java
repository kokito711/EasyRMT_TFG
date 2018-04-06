package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import com.Sergio.EasyRMT.Service.Converter.ProjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ReqTypeRepository reqTypeRepository;
    @Autowired
    ProjectConverter projectConverter;

    @Transactional(readOnly = true)
    public List<ProjectDom> getProjects(){
        List<Project> projectModelList = projectRepository.findAll();
        List<ProjectDom> projectDomList = projectConverter.toDomain(projectModelList);
        return projectDomList;
    }

}
