package com.satnam.codesapi.user_service.repository;

import com.satnam.codesapi.user_service.entity.ReviewCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCacheRepository extends CrudRepository<ReviewCache, String> {
}
