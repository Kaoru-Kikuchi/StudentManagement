package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出されること() {

    // ① 準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList())
        .thenReturn(studentCourseList);
    when(converter.convertStudentDetails(
        studentList,
        studentCourseList))
        .thenReturn(expected);

    // ② 実行
    List<StudentDetail> actual = sut.searchStudentList();

    // ③ 確認
    assertSame(expected, actual);

    verify(repository, times(1)).search();
    verify(repository, times(1))
        .searchStudentCourseList();
    verify(converter, times(1))
        .convertStudentDetails(
            studentList,
            studentCourseList);
  }

  @Test
  void 受講生詳細検索_IDに紐づく受講生と受講コースが取得されること() {

    // ① 準備
    String studentId = "student-id";

    Student student = new Student();
    student.setId(studentId);
    student.setName("山田太郎");

    List<StudentCourse> studentCourseList = new ArrayList<>();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName("Javaコース");

    studentCourseList.add(studentCourse);

    when(repository.searchStudent(studentId))
        .thenReturn(student);
    when(repository.searchStudentCourseListByStudentId(studentId))
        .thenReturn(studentCourseList);

    // ② 実行
    StudentDetail actual = sut.searchStudent(studentId);

    // ③ 確認
    assertSame(student, actual.getStudent());
    assertSame(
        studentCourseList,
        actual.getStudentCourseList());

    verify(repository, times(1))
        .searchStudent(studentId);
    verify(repository, times(1))
        .searchStudentCourseListByStudentId(studentId);
  }

  @Test
  void 受講生詳細登録_受講生と受講コースに必要な情報が設定され登録されること() {

    // ① 準備
    Student student = new Student();
    student.setName("山田太郎");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Javaコース");

    List<StudentCourse> studentCourseList =
        new ArrayList<>();
    studentCourseList.add(studentCourse);

    StudentDetail studentDetail =
        new StudentDetail(student, studentCourseList);

    LocalDateTime before = LocalDateTime.now();

    // ② 実行
    StudentDetail actual =
        sut.registerStudent(studentDetail);

    LocalDateTime after = LocalDateTime.now();

    // ③ 確認
    assertSame(studentDetail, actual);

    // 受講生にUUIDが設定されていること
    assertNotNull(student.getId());
    assertFalse(student.getId().isBlank());

    // 受講コースにもUUIDが設定されていること
    assertNotNull(studentCourse.getId());
    assertFalse(studentCourse.getId().isBlank());

    // 受講コースに受講生IDが設定されていること
    assertEquals(
        student.getId(),
        studentCourse.getStudentId());

    // コース開始日が現在時刻の範囲内であること
    assertFalse(
        studentCourse.getCourseStartAt()
            .isBefore(before));

    assertFalse(
        studentCourse.getCourseStartAt()
            .isAfter(after));

    // コース終了日が開始日の1年後であること
    assertEquals(
        studentCourse.getCourseStartAt().plusYears(1),
        studentCourse.getCourseEndAt());

    verify(repository, times(1))
        .registerStudent(student);
    verify(repository, times(1))
        .registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細更新_受講生とすべての受講コースが更新されること() {

    // ① 準備
    Student student = new Student();
    student.setId("student-id");
    student.setName("山田太郎");

    StudentCourse firstCourse =
        new StudentCourse();
    firstCourse.setId("course-id-1");
    firstCourse.setCourseName("Javaコース");

    StudentCourse secondCourse =
        new StudentCourse();
    secondCourse.setId("course-id-2");
    secondCourse.setCourseName("AWSコース");

    List<StudentCourse> studentCourseList =
        new ArrayList<>();

    studentCourseList.add(firstCourse);
    studentCourseList.add(secondCourse);

    StudentDetail studentDetail =
        new StudentDetail(student, studentCourseList);

    // ② 実行
    sut.updateStudent(studentDetail);

    // ③ 確認
    verify(repository, times(1))
        .updateStudent(student);

    verify(repository, times(1))
        .updateStudentCourse(firstCourse);

    verify(repository, times(1))
        .updateStudentCourse(secondCourse);
  }
}