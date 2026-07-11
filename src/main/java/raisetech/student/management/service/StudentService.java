package raisetech.student.management.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.repository.StudentRepository;

@Slf4j
@Service
public class StudentService {
  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search().stream()
        .filter(student ->student.getAge() >= 30)
        .filter(student ->student.getAge() < 40)
        .toList();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourseList().stream()
        .filter(studentCourse -> "Java基礎".equals(studentCourse.getCourseName()))
        .toList();
    };
  }

