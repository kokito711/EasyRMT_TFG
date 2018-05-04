/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.Group_UserKey;
import com.Sergio.EasyRMT.Model.Group_user;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupUserRepository extends JpaRepository<Group_user, Group_UserKey> {
}
