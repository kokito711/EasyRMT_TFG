/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.UserStoryRepository;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserStoryService {
    ObjectRepository objectRepository;
    EpicRepository epicRepository;
    ProjectRepository projectRepository;
    UserStoryRepository userStoryRepository;
    UserStoryConverter userStoryConverter;

    @Autowired
    public UserStoryService(ObjectRepository objectRepository, EpicRepository epicRepository,
                            ProjectRepository projectRepository, UserStoryRepository userStoryRepository, UserStoryConverter userStoryConverter) {
        this.objectRepository = objectRepository;
        this.epicRepository = epicRepository;
        this.projectRepository = projectRepository;
        this.userStoryRepository = userStoryRepository;
        this.userStoryConverter = userStoryConverter;
    }

    @Transactional(readOnly = true)
    public int getCountUserStories(int epicId){
        int count = userStoryRepository.countUserStories(epicId);
        return count;
    }
}
