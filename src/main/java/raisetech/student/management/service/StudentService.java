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
 * 受講生情報を取り扱うサービスです。
 * 検索、登録、更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  /**
   * コンストラクタ。
   *
   * @param repository 受講生リポジトリ
   * @param converter 受講生コンバーター
   */
  @Autowired
  public StudentService(
      StudentRepository repository,
      StudentConverter converter) {

    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 受講生情報と受講生コース情報を取得し、受講生詳細一覧に変換します。
   *
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList =
        repository.searchStudentCourseList();

    return converter.convertStudentDetails(
        studentList,
        studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報と受講生コース情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);

    List<StudentCourse> studentCourseList =
        repository.searchStudentCourseListByStudentId(student.getId());

    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、
   * 受講生コース情報に受講生ID、コース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を設定した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    student.setId(UUID.randomUUID().toString());

    repository.registerStudent(student);

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      studentCourse.setId(UUID.randomUUID().toString());
      initStudentCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);
    });

    return studentDetail;
  }

  /**
   * 受講生コース情報の登録に必要な初期情報を設定します。
   *
   * @param studentCourse 受講生コース情報
   * @param student 受講生
   */
  private void initStudentCourse(
      StudentCourse studentCourse,
      Student student) {

    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    studentDetail.getStudentCourseList()
        .forEach(repository::updateStudentCourse);
  }
}