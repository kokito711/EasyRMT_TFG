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

   @Query(value = "SELECT count(object.idobject) FROM " +
           "(easyrmt.object inner join easyrmt.feature on object.idobject = feature.idobject)" +
           "inner join easyrmt.project on project_idproject = project.idproject\n" +
           "and project.idproject = ?1 group by feature.state", nativeQuery = true)
   List<Integer> getFeaturesState(int projectId);

    @Query(value = "SELECT count(object.idobject) FROM " +
            "(easyrmt.object inner join easyrmt.epic on object.idobject = epic.idobject)" +
            "inner join easyrmt.project on project_idproject = project.idproject\n" +
            "and project.idproject = ?1 group by epic.state", nativeQuery = true)
    List<Integer> getEpicsState(int projectId);

    @Query(value = "SELECT count(object.idobject) FROM " +
            "(easyrmt.object inner join easyrmt.use_case on object.idobject = use_case.idobject)" +
            "inner join easyrmt.project on project_idproject = project.idproject\n" +
            "and project.idproject = ?1 group by use_case.state", nativeQuery = true)
    List<Integer> getUseCasesState(int projectId);

    @Query(value = "SELECT count(object.idobject) FROM " +
            "(easyrmt.object inner join easyrmt.user_story on object.idobject = user_story.idobject)" +
            "inner join easyrmt.project on project_idproject = project.idproject\n" +
            "and project.idproject = ?1 group by user_story.state", nativeQuery = true)
    List<Integer> getUserStoriesState(int projectId);

    @Query(value = "SELECT count(object.idobject) FROM " +
            "(easyrmt.object inner join easyrmt.requirement on object.idobject = requirement.idobject)" +
            "inner join easyrmt.project on project_idproject = project.idproject\n" +
            "and project.idproject = ?1 group by requirement.state", nativeQuery = true)
    List<Integer> getRequirementsState(int projectId);

}
