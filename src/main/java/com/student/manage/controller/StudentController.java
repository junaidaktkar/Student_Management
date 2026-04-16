 package com.student.manage.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.manage.dao.StudentDao;
import com.student.manage.entity.Student;
import com.student.manage.service.StudentService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/student")
public class StudentController {

    private final StudentDao studentDao;

	//Service class
	StudentService service;
	
	public StudentController(StudentService service, StudentDao studentDao) {
		super();
		this.service = service;
		this.studentDao = studentDao;
	}
	
	@GetMapping("/get")
	public CompletableFuture<ResponseEntity<List<Student>>> getStudents() {
		CompletableFuture<List<Student>> future=service.getAllStudents();
		
		return future.thenApply(s->ResponseEntity.status(HttpStatus.OK).body(s))
				.exceptionally(t->{
					System.out.println(t.getMessage());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				});
	}
	
	@GetMapping("get/{id}")
	public ResponseEntity<Student> getById(@PathVariable int id) {
		return new ResponseEntity<Student>(service.getById(id), HttpStatusCode.valueOf(200));
	}
	
	@PostMapping("/create")
	public CompletableFuture<ResponseEntity<?>> create(@Valid @RequestBody Student std) {
		return service.create(std)
				.handle((s,exception)->{
					return ResponseEntity.status(HttpStatus.OK).body(s);
				});
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Student> update(@PathVariable Integer id, @RequestBody Student std) {
		Student s=service.update(id, std);
		if(s==null) {
			System.out.println("Student not found to update for id: "+id);
			return ResponseEntity.status(404).build();
		}
		System.out.println(s);
		return ResponseEntity.status(201).body(s);
	}
	
	@GetMapping("/course/{name}")
	//derived query
	public List<Student> getByCourse(@PathVariable String name) {
		return service.findByCourse(name) ;
	}
	
	@GetMapping("/marks/{id}")
	//derived query
	public List<Student> getAllMarksGreaterThan(@PathVariable("id") Integer i) {
		System.out.println(i);
		return service.findAllMarksGreaterThan(i);
	}
	
	@GetMapping("/marksandcourse")
	public List<Student> getStudentsByMarksAndCourse(@RequestParam("marks") Integer m,@RequestParam("course") String c) {
		return service.findByMarksAndCourse(m,c);
	}
	
	@GetMapping("/maxmarks")
	public ResponseEntity<Student> maxMarks() {
		return ResponseEntity.status(HttpStatus.OK).body(service.maxMarks());
	}
	
	
//	@ExceptionHandler(StudentNotFoundException.class )
//	public ResponseEntity<ErrorResponse> studentNotFoundException(StudentNotFoundException e) {
//		ErrorResponse error=new ErrorResponse(e.getMessage(),HttpStatus.NOT_FOUND, LocalDateTime.now());
//		return new ResponseEntity<>(error ,HttpStatus.NOT_FOUND);
//	}
//	
}




