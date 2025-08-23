package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
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

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      studentCourse.setCourseStartAt(LocalDateTime.now());
      studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourse(studentCourse);
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudent(studentDetail.getStudent());

    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentCourse(studentCourse);
    }
  }

  @Transactional(readOnly = true)
  public StudentDetail getStudentDetail(String studentId) {
    Student student = repository.findById(studentId);
    List<StudentCourse> courses = repository.searchStudentsCourses().stream()
        .filter(c -> c.getStudentId().equals(student.getId()))
        .toList();
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourse(courses);
    return detail;
  }

}
