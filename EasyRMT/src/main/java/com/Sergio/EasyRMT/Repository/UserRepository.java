/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Override
    @Query(value = "SELECT * FROM easyrmt.app_user where not username = 'Admin'", nativeQuery = true)
    List<User> findAll();
}
