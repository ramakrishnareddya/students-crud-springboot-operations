package com.college.students.services;


import org.springframework.data.jpa.repository.JpaRepository;

import com.college.students.models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
