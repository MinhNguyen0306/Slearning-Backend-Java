package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {

    @Query(value = "SELECT * FROM sub_category s where s.category_id = :categoryId", nativeQuery = true)
    Page<SubCategory> findSubOfCategory(Pageable pageable, @Param("categoryId") UUID categoryId);
}
