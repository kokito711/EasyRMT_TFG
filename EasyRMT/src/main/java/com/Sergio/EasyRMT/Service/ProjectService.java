package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.ReqTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ReqTypeRepository reqTypeRepository;

    @Transactional(readOnly = true)
    public String getProjects(){
        List
    }

}
