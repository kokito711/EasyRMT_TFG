/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Integer> {
    @Query(value = "SELECT requirement.* FROM (easyrmt.requirement inner join easyrmt.object on " +
            "requirement.idobject = object.idobject) inner join easyrmt.project on " +
            "object.project_idproject = project.idproject and project.idproject = ?1",
    nativeQuery = true)
    List<Requirement> findByProjectID(int projectId);
}
