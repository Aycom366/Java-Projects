package com.aycom.elearning.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.elearning.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAllByFirstName(String firstName);

    List<Author> findAllByFirstNameIgnoreCase(String firstName);

    List<Author> findAllByFirstNameContainingIgnoreCase(String firstName);

    List<Author> findAllByFirstNameStartsWithIgnoreCase(String firstName);

    // select * from author where first_name in ('ali','bou')
    List<Author> findAllByFirstNameInIgnoreCase(List<String> firstNames);

}
