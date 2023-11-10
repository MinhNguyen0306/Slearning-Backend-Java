package com.example.Slearning.Backend.Java.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchRepository {

    private final EntityManagerFactory entityManagerFactory;

    public List<Object[]> searchCoursesAndUsers(String searchKey) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT c, u FROM Course c, User u" +
                "WHERE c.id = u.id AND c.courseTitle LIKE %:searchKey% AND u.fullName LIKE %:searchKey%");
        query.setParameter("searchKey", searchKey);

        return query.getResultList();
    }
}
