/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    @Query(value = "SELECT feature.* FROM ((easyrmt.feature inner join easyrmt.object on feature.idobject = object.idobject)" +
            " inner join easyrmt.project on object.project_idproject = project.idproject and project.idproject = ?1)",
    nativeQuery = true)
    List<Feature> findByProjectID(int projectId);
}
