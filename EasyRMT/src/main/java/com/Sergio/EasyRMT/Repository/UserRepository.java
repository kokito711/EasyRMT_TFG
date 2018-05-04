/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "SELECT * FROM (( easyrmt.app_user inner join easyrmt.user_role  on app_user.user_id = user_role.user_id) " +
            "inner join easyrmt.role on user_role.role_id = role.role_id and not role ='ADMIN')", nativeQuery = true)
    List<User> findNotAdmin();

    @Override
    @Query(value = "SELECT * FROM easyrmt.app_user where not username = 'Admin'", nativeQuery = true)
    List<User> findAll();

    @Modifying
    @Query(value = "DELETE FROM easyrmt.app_user WHERE user_Id = ?1", nativeQuery = true)
    void deleteUser(int userId);
}
