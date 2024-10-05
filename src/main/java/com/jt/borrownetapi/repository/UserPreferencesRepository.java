package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
}