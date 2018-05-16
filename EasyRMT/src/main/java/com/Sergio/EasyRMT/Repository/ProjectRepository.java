/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "select * from easyrmt.project where project.group_groupId = ?1", nativeQuery = true)
    List<Project> findByGroup(int groupId);
    Optional<Project> findByIdProject(int id);
    @Modifying
    @Query(value = "DELETE FROM easyrmt.project WHERE idproject=?1", nativeQuery = true)
    void deleteProjectByIdProject(int id);
    boolean existsByIdProject(int id);


   @Query(value = "SELECT count(object.idobject) FROM (easyrmt.object inner join easyrmt.Traceability on " +
           "object.idobject = Traceability.Object1) inner join easyrmt.project on project_idproject = project.idproject " +
           "and project.idproject = ?1", nativeQuery = true)
   int getTracedObjects(int projectId);
   @Query(value = "SELECT count(object.idobject) FROM easyrmt.object inner join easyrmt.project on" +
           " project_idproject = project.idproject and project.idproject = ?1 where not exists " +
           "(select Traceability.Object1 from easyrmt.Traceability where object.idobject = Traceability.Object1)",
           nativeQuery = true)
    int getNotTracedObjects(int projectId);
}
