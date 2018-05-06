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
}
