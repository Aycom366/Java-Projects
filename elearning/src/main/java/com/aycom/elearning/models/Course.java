package com.aycom.elearning.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Course extends BaseEntity {

    private String courseName;
    private String courseDescription;

    @ManyToMany
    @JoinTable(name = "authors_courses", joinColumns = {
            @JoinColumn(name = "course_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "author_id")
    }) // an automatic table will be created
    private List<Author> authors;

    @OneToMany(mappedBy = "course")
    private List<Section> sections;

}
