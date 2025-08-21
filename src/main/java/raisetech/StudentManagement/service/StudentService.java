package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentCourse> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  public void saveStudent(Student student) {
    repository.saveStudent(student);
  }

  public void saveStudentCourse(StudentCourse studentCourse) {
    repository.saveStudentCourse(studentCourse);
  }

  public int getMaxStudentId() {
    return repository.findMaxStudentId(); // Repository で取得するメソッド
  }

  // 最大 student_course id を取得
  public int getMaxStudentCourseId() {
    return repository.findMaxStudentCourseId();
  }
}
