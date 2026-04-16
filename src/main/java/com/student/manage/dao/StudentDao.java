package com.student.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.student.manage.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, Integer>{
	
	//Find All by name who purchase given course 
	public List<Student> findAllByCourse(String s);
	
	//Find all students where marks greater than given value
	public List<Student> findAllByMarksGreaterThanEqual(Integer i);
	
	//JPQL
	@Query( "select s from Student s Where marks >= :m AND course= :c" )
	public List<Student> findAllByCourseAndMarks(@Param("m")Integer marks,@Param("c") String course );

	//Class toppers
	@Query("select s from Student s order by s.marks DESC")
	public List<Student> findTopper();
	
	//NAtive Query
	@Query(nativeQuery = true, value="select * from student order by marks DESC limit 1")
	public Student findTopStudent();
	

}
