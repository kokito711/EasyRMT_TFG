/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UseCaseRepository extends JpaRepository<UseCase, Integer> {
    @Query(value = "SELECT use_case.* FROM easyrmt.use_case INNER join easyrmt.feature on" +
            " use_case.feature = easyrmt.feature.idobject and easyrmt.feature.idobject = ?1",nativeQuery = true)
    List<UseCase> findByFeatureId(int featureId);
    @Query(value = "SELECT use_case.* FROM easyrmt.use_case inner join easyrmt.object on " +
            "object.idobject = use_case.idobject and object.project_idproject = ?1", nativeQuery = true)
    List<UseCase> findByProjectId(int projectId);
}
