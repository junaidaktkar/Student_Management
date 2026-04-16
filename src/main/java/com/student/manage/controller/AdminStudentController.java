package com.student.manage.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.manage.entity.Student;
import com.student.manage.service.StudentService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/admin")
//@Profile("dev")
@ConditionalOnProperty(name = "spring.profiles.active", havingValue="dev", matchIfMissing = false)

//Sensetive data only environment authorize can see
public class AdminStudentController {
	StudentService service;
	
	public AdminStudentController(StudentService service) {
		super();
		this.service = service;
	}

	@PostMapping("/details")
	public String postMethodName() {
		return "Admin LoggedIn..";
	}
	
	@GetMapping("/get")
	public CompletableFuture<ResponseEntity<List<Student>>> getStudents() {
		return service.getAllStudents().thenApply(s->ResponseEntity.ok(s));
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
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Integer id){
			service.delete(id);
			return ResponseEntity.ok().body("Student deleted from this id : "+id );
	}
	
	@GetMapping("/c")
	public void cache() {
		service.cacheDetails();
	}
	
	@GetMapping("/testException")
	public void getExpection() {
		service.testException();
	}
	
}
