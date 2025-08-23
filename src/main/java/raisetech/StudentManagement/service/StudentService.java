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

    // 学生情報を更新
    repository.updateStudent(studentDetail.getStudent());

    // コース情報を更新
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());

      // 既存レコードを取得
      StudentCourse existing = repository.findStudentCourseById(studentCourse.getId());

      // start/end が null なら既存値をセット（最小変更）
      if (studentCourse.getCourseStartAt() == null) {
        studentCourse.setCourseStartAt(existing.getCourseStartAt());
      }
      if (studentCourse.getCourseEndAt() == null) {
        studentCourse.setCourseEndAt(existing.getCourseEndAt());
      }

      // 更新
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

  // 学生コースを更新するメソッド
  public void updateCourse(StudentCourse studentCourse) {
    // DB から既存の情報を取得
    StudentCourse existing = repository.findStudentCourseById(studentCourse.getId());

    // start/end が null なら既存の値をセット
    if (studentCourse.getCourseStartAt() == null) {
      studentCourse.setCourseStartAt(existing.getCourseStartAt());
    }
    if (studentCourse.getCourseEndAt() == null) {
      studentCourse.setCourseEndAt(existing.getCourseEndAt());
    }

    // DB を更新
    repository.updateStudentCourse(studentCourse);
  }

}
