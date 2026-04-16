package com.student.manage.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotNull(message = "Age is required")
	@Max(40)
	private Integer age;
	
	@NotNull(message = "Marks are required")
	@Max(100)
	private Integer marks;
	
	@NotBlank(message = "Course Name is required")
	private String course;

	public Student(Integer id, String name, Integer age, Integer marks, String course) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.marks = marks;
		this.course = course;
	}

	public Student() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getMarks() {
		return marks;
	}

	public void setMarks(Integer marks) {
		this.marks = marks;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", marks=" + marks + ", course=" + course
				+ "]";
	}

}










