/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReqTypeRepository extends JpaRepository<RequirementType,Integer> {
        Optional<RequirementType> findByName(String name);

}
