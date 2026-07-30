package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;

/**
 * 受講生テーブルと受講生コーステーブルに紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧
   */
  List<Student> search();

  /**
   * 受講生IDに紐づく受講生を検索します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース情報一覧
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報一覧
   */
  List<StudentCourse> searchStudentCourseListByStudentId(String studentId);

  /**
   * 受講生を新規登録します。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生情報を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コースIDに紐づく申込状況を検索します。
   *
   * @param studentCourseId 受講生コースID
   * @return 申込状況
   */
  StudentCourseStatus searchStudentCourseStatus(String studentCourseId);

  /**
   * 申込状況を登録します。
   *
   * @param studentCourseStatus 申込状況
   */
  void registerStudentCourseStatus(StudentCourseStatus studentCourseStatus);

  /**
   * 申込状況を更新します。
   *
   * @param studentCourseStatus 申込状況
   */
  void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus);
}
