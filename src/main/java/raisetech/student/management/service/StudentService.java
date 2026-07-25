package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取扱うサービスです。
 * 検索、登録、更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }
  /**
   * 受講生一覧検索です。
   * 全件検索は行わないので、条件指定は行わないものになります。
   *
   * @return　受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
   List<StudentCourse> studentCoursesList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、この受講生に紐づくコース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(String id) {
  Student student = repository.searchStudent(id);
 List<StudentCourse> studentCourses = repository.searchStudentCourses(student.getId());
 return new StudentDetail(student,studentCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {

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
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      repository.updateStudentCourse(studentCourse);
    }
  }
}