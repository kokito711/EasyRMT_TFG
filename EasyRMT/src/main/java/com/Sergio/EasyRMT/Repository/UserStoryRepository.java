/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Repository;

import com.Sergio.EasyRMT.Model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserStoryRepository extends JpaRepository<UserStory, Integer> {
    @Query(value = "SELECT user_story.* FROM easyrmt.user_story INNER join easyrmt.epic on" +
            " user_story.epic = easyrmt.epic.idobject and easyrmt.epic.idobject = ?1",nativeQuery = true)
    List<UserStory> findByEpicId(int epicId);
}
