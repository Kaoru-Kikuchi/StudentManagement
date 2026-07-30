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
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

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

    when(repository.search())
        .thenReturn(studentList);

    when(repository.searchStudentCourseList())
        .thenReturn(studentCourseList);

    when(converter.convertStudentDetails(
        studentList,
        studentCourseList))
        .thenReturn(expected);

    // ② 実行
    List<StudentDetail> actual =
        sut.searchStudentList();

    // ③ 確認
    assertSame(expected, actual);

    verify(repository, times(1))
        .search();

    verify(repository, times(1))
        .searchStudentCourseList();

    verify(converter, times(1))
        .convertStudentDetails(
            studentList,
            studentCourseList);
  }

  @Test
  void 受講生詳細の一覧検索_各受講生コースに申込状況が設定されること() {

    // ① 準備
    Student student = new Student();
    student.setId("student-id");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("course-id");
    studentCourse.setStudentId("student-id");
    studentCourse.setCourseName("Javaコース");

    StudentCourseStatus studentCourseStatus =
        new StudentCourseStatus();

    studentCourseStatus.setId("status-id");
    studentCourseStatus.setStudentCourseId("course-id");
    studentCourseStatus.setStatus("受講中");

    List<Student> studentList =
        List.of(student);

    List<StudentCourse> studentCourseList =
        List.of(studentCourse);

    List<StudentDetail> expected =
        new ArrayList<>();

    when(repository.search())
        .thenReturn(studentList);

    when(repository.searchStudentCourseList())
        .thenReturn(studentCourseList);

    when(repository.searchStudentCourseStatus("course-id"))
        .thenReturn(studentCourseStatus);

    when(converter.convertStudentDetails(
        studentList,
        studentCourseList))
        .thenReturn(expected);

    // ② 実行
    List<StudentDetail> actual =
        sut.searchStudentList();

    // ③ 確認
    assertSame(expected, actual);

    assertSame(
        studentCourseStatus,
        studentCourse.getStudentCourseStatus());

    verify(repository, times(1))
        .searchStudentCourseStatus("course-id");
  }

  @Test
  void 受講生詳細検索_IDに紐づく受講生と受講コースと申込状況が取得されること() {

    // ① 準備
    String studentId = "student-id";
    String studentCourseId = "course-id";

    Student student = new Student();
    student.setId(studentId);
    student.setName("山田太郎");

    StudentCourse studentCourse =
        new StudentCourse();

    studentCourse.setId(studentCourseId);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName("Javaコース");

    StudentCourseStatus studentCourseStatus =
        new StudentCourseStatus();

    studentCourseStatus.setId("status-id");
    studentCourseStatus.setStudentCourseId(studentCourseId);
    studentCourseStatus.setStatus("本申込");

    List<StudentCourse> studentCourseList =
        new ArrayList<>();

    studentCourseList.add(studentCourse);

    when(repository.searchStudent(studentId))
        .thenReturn(student);

    when(repository.searchStudentCourseListByStudentId(studentId))
        .thenReturn(studentCourseList);

    when(repository.searchStudentCourseStatus(studentCourseId))
        .thenReturn(studentCourseStatus);

    // ② 実行
    StudentDetail actual =
        sut.searchStudent(studentId);

    // ③ 確認
    assertSame(
        student,
        actual.getStudent());

    assertSame(
        studentCourseList,
        actual.getStudentCourseList());

    assertSame(
        studentCourseStatus,
        actual.getStudentCourseList()
            .get(0)
            .getStudentCourseStatus());

    verify(repository, times(1))
        .searchStudent(studentId);

    verify(repository, times(1))
        .searchStudentCourseListByStudentId(studentId);

    verify(repository, times(1))
        .searchStudentCourseStatus(studentCourseId);
  }

  @Test
  void 受講生詳細登録_申込状況が未設定の場合は仮申込として登録されること() {

    // ① 準備
    Student student = new Student();
    student.setName("山田太郎");

    StudentCourse studentCourse =
        new StudentCourse();

    studentCourse.setCourseName("Javaコース");

    List<StudentCourse> studentCourseList =
        new ArrayList<>();

    studentCourseList.add(studentCourse);

    StudentDetail studentDetail =
        new StudentDetail(
            student,
            studentCourseList);

    LocalDateTime before =
        LocalDateTime.now();

    // ② 実行
    StudentDetail actual =
        sut.registerStudent(studentDetail);

    LocalDateTime after =
        LocalDateTime.now();

    // ③ 確認
    assertSame(studentDetail, actual);

    // 受講生にUUIDが設定されていること
    assertNotNull(student.getId());
    assertFalse(student.getId().isBlank());

    // 受講コースにUUIDが設定されていること
    assertNotNull(studentCourse.getId());
    assertFalse(studentCourse.getId().isBlank());

    // 受講コースに受講生IDが設定されていること
    assertEquals(
        student.getId(),
        studentCourse.getStudentId());

    // コース開始日時が実行時間内であること
    assertFalse(
        studentCourse.getCourseStartAt()
            .isBefore(before));

    assertFalse(
        studentCourse.getCourseStartAt()
            .isAfter(after));

    // コース終了日時が開始日時の1年後であること
    assertEquals(
        studentCourse.getCourseStartAt()
            .plusYears(1),
        studentCourse.getCourseEndAt());

    StudentCourseStatus studentCourseStatus =
        studentCourse.getStudentCourseStatus();

    // 申込状況が自動生成されていること
    assertNotNull(studentCourseStatus);

    // 申込状況にUUIDが設定されていること
    assertNotNull(studentCourseStatus.getId());
    assertFalse(
        studentCourseStatus.getId().isBlank());

    // 受講生コースIDが設定されていること
    assertEquals(
        studentCourse.getId(),
        studentCourseStatus.getStudentCourseId());

    // 初期値が仮申込であること
    assertEquals(
        "仮申込",
        studentCourseStatus.getStatus());

    verify(repository, times(1))
        .registerStudent(student);

    verify(repository, times(1))
        .registerStudentCourse(studentCourse);

    verify(repository, times(1))
        .registerStudentCourseStatus(
            studentCourseStatus);
  }

  @Test
  void 受講生詳細登録_指定された申込状況が登録されること() {

    // ① 準備
    Student student = new Student();
    student.setName("山田太郎");

    StudentCourseStatus studentCourseStatus =
        new StudentCourseStatus();

    studentCourseStatus.setStatus("本申込");

    StudentCourse studentCourse =
        new StudentCourse();

    studentCourse.setCourseName("Javaコース");
    studentCourse.setStudentCourseStatus(
        studentCourseStatus);

    StudentDetail studentDetail =
        new StudentDetail(
            student,
            List.of(studentCourse));

    // ② 実行
    sut.registerStudent(studentDetail);

    // ③ 確認
    assertSame(
        studentCourseStatus,
        studentCourse.getStudentCourseStatus());

    assertEquals(
        "本申込",
        studentCourseStatus.getStatus());

    assertNotNull(studentCourseStatus.getId());

    assertEquals(
        studentCourse.getId(),
        studentCourseStatus.getStudentCourseId());

    verify(repository, times(1))
        .registerStudentCourseStatus(
            studentCourseStatus);
  }

  @Test
  void 受講生詳細更新_受講生とすべての受講コースと申込状況が更新されること() {

    // ① 準備
    Student student = new Student();
    student.setId("student-id");
    student.setName("山田太郎");

    StudentCourseStatus firstStatus =
        new StudentCourseStatus();

    firstStatus.setId("status-id-1");
    firstStatus.setStudentCourseId("course-id-1");
    firstStatus.setStatus("受講中");

    StudentCourse firstCourse =
        new StudentCourse();

    firstCourse.setId("course-id-1");
    firstCourse.setCourseName("Javaコース");
    firstCourse.setStudentCourseStatus(
        firstStatus);

    StudentCourseStatus secondStatus =
        new StudentCourseStatus();

    secondStatus.setId("status-id-2");
    secondStatus.setStudentCourseId("course-id-2");
    secondStatus.setStatus("受講終了");

    StudentCourse secondCourse =
        new StudentCourse();

    secondCourse.setId("course-id-2");
    secondCourse.setCourseName("AWSコース");
    secondCourse.setStudentCourseStatus(
        secondStatus);

    List<StudentCourse> studentCourseList =
        new ArrayList<>();

    studentCourseList.add(firstCourse);
    studentCourseList.add(secondCourse);

    StudentDetail studentDetail =
        new StudentDetail(
            student,
            studentCourseList);

    // ② 実行
    sut.updateStudent(studentDetail);

    // ③ 確認
    verify(repository, times(1))
        .updateStudent(student);

    verify(repository, times(1))
        .updateStudentCourse(firstCourse);

    verify(repository, times(1))
        .updateStudentCourse(secondCourse);

    verify(repository, times(1))
        .updateStudentCourseStatus(firstStatus);

    verify(repository, times(1))
        .updateStudentCourseStatus(secondStatus);
  }

  @Test
  void 受講生詳細更新_申込状況が未設定の場合は申込状況の更新を行わないこと() {

    // ① 準備
    Student student = new Student();
    student.setId("student-id");

    StudentCourse studentCourse =
        new StudentCourse();

    studentCourse.setId("course-id");
    studentCourse.setCourseName("Javaコース");

    StudentDetail studentDetail =
        new StudentDetail(
            student,
            List.of(studentCourse));

    // ② 実行
    sut.updateStudent(studentDetail);

    // ③ 確認
    verify(repository, times(1))
        .updateStudent(student);

    verify(repository, times(1))
        .updateStudentCourse(studentCourse);

    verify(repository, never())
        .updateStudentCourseStatus(any());
  }
}