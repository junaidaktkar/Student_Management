package com.student.manage.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.student.manage.dao.StudentDao;
import com.student.manage.entity.Student;
import com.student.manage.exceptions.StudentNotFoundException;

@Service
public class StudentService {
	StudentDao studentDao;
	
	CacheManager c;
	
	//Dao DI
	public StudentService(StudentDao studentDao,CacheManager c) {
		super();
		this.studentDao = studentDao;
		this.c=c;
	}
	
	@Async
	public void testException() {
		System.out.println(1/0);
	}

	//if dont give executor name it takes default
	@Async
	@Cacheable(value = "students", key = "'all'")
	public CompletableFuture<List<Student>> getAllStudents() {
		System.out.println("Service getAll method called....");
//		System.out.println("New Async Thread: "+Thread.currentThread().getName());
		return CompletableFuture.completedFuture(studentDao.findAll());
	}
	
	//get student by id
	@Cacheable(value = "students", key = "#id")
	public Student getById(int id) {
		System.out.println("Service getById methods called....");
		return studentDao.findById(id).orElseThrow(()->new StudentNotFoundException("Student Not Found :"+id));
	}
	
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
	@Async
	public CompletableFuture<Student> create(Student std) {
		return CompletableFuture.completedFuture(studentDao.save(std));
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@CachePut(value = "students", key = "#id")
	@CacheEvict(value = "students", key = "'all'")
	public Student update(int id,Student std) {
		Student s=null;
		try {
			s=getById(id);
			System.out.println(s);
			if(s!=null)
			if(std.getName()!=null )
				s.setName(std.getName());
			if(std.getAge()!=null)
				s.setAge(std.getAge());
			if(std.getMarks()!=null)
				s.setMarks(std.getMarks());
			if(std.getCourse()!=null)
				s.setCourse(std.getCourse());
			System.out.println(s);
			
		}catch (NoSuchElementException e) {
			throw new StudentNotFoundException("Student Not Found :"+id);
		}
		
		return studentDao.save(s);
	}
	
	@Transactional
	@Caching(evict = {
		    @CacheEvict(value = "students", key = "#id", beforeInvocation = false),
		    @CacheEvict(value = "students", key = "'all'",beforeInvocation = false)
		    
		})
	public void delete(Integer id) {
		 if (!studentDao.existsById(id)) {
		        throw new StudentNotFoundException("Student Not Found: " + id);
		    }
		 studentDao.deleteById(id);
	}
	
	public void cacheDetails() {
		Cache cache = c.getCache("students");
		if(cache!=null) {
			System.out.println(cache.getName());
			System.out.println(cache.getNativeCache().toString());
		}
		
	}

	public List<Student> findByCourse(String name) {
		return studentDao.findAllByCourse(name);
	}

	public List<Student> findAllMarksGreaterThan(Integer i) {

		return studentDao.findAllByMarksGreaterThanEqual(i);
	}

	public List<Student> findByMarksAndCourse(Integer m, String c) {
		return studentDao.findAllByCourseAndMarks(m, c);
	}

	public Student maxMarks() {
		
		return studentDao.findTopStudent();
	}


	

}
