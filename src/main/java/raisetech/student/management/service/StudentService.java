package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

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

  public StudentDetail searchStudent(String id) {
  Student student = repository.searchStudent(id);
 List<StudentCourse> studentCourses = repository.searchStudentCourses(student.getId());
 StudentDetail studentDetail =new StudentDetail();
 studentDetail.setStudent(student);
 studentDetail.setStudentCourses(studentCourses);
 return studentDetail;
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourseList();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {

    Student student = studentDetail.getStudent();
    student.setId(UUID.randomUUID().toString());

    repository.registerStudent(student);

    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setId(UUID.randomUUID().toString());
      studentCourse.setStudentId(student.getId());
      studentCourse.setCourseStartAt(LocalDateTime.now());
      studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

      repository.registerStudentCourse(studentCourse);
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      repository.updateStudentCourse(studentCourse);
    }
  }
}