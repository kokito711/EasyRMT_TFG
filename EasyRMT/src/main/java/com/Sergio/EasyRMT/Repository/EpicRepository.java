/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EpicRepository extends JpaRepository<Epic, Integer> {
    @Query(value = "SELECT * FROM ((easyrmt.epic inner join easyrmt.object on epic.idobject = object.idobject) " +
            "inner join easyrmt.project on object.project_idproject = project.idproject and idproject = ?1)",
    nativeQuery = true)
    List<Epic> findByProjectID(int projectId);

    @Query(value = "SELECT count(epic.idobject) FROM ((easyrmt.epic inner join easyrmt.object on "+
            "(epic.idobject = object.idobject)inner join easyrmt.project on object.project_idproject = project.idproject"+
            "and project_idproject = ?1)", nativeQuery = true)
    int countEpics(int projectId);
}
