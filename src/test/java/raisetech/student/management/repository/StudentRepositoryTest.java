package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {

    List<Student> actual = sut.search();

    assertThat(actual).hasSize(9);

    assertThat(actual)
        .extracting(Student::getId)
        .contains(
            "61e9a148-4fea-4735-8065-0539daa9f3fc",
            "7cc22a59-7ae9-4cc0-853a-c10b3b43922a",
            "88c6624b-f736-4ed0-9729-b5476068ddab",
            "a19296b7-4856-40e0-aa81-c38428e13030");
  }

  @Test
  void 受講生IDに紐づく受講生を検索できること() {

    String studentId =
        "61e9a148-4fea-4735-8065-0539daa9f3fc";

    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(studentId);
    assertThat(actual.getName()).isEqualTo("徳水進");
    assertThat(actual.getKanaName()).isEqualTo("トクミズ");
    assertThat(actual.getNickname()).isEqualTo("トックン");
    assertThat(actual.getEmail()).isEqualTo("TT@outlook.jp");
    assertThat(actual.getArea()).isEqualTo("永野");
    assertThat(actual.getAge()).isEqualTo(50);
    assertThat(actual.getSex()).isEqualTo("男性");
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {

    List<StudentCourse> actual =
        sut.searchStudentCourseList();

    assertThat(actual).hasSize(9);

    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .contains(
            "データベース",
            "HTML/CSS",
            "Java基礎",
            "Spring Boot応用",
            "Spring Boot",
            "Java応用");
  }

  @Test
  void 受講生IDに紐づく受講生コース情報を検索できること() {

    String studentId =
        "61e9a148-4fea-4735-8065-0539daa9f3fc";

    List<StudentCourse> actual =
        sut.searchStudentCourseListByStudentId(studentId);

    assertThat(actual).hasSize(1);

    StudentCourse studentCourse = actual.get(0);

    assertThat(studentCourse.getStudentId())
        .isEqualTo(studentId);

    assertThat(studentCourse.getCourseName())
        .isEqualTo("データベース");
  }

  @Test
  void 受講生の登録が行えること() {

    Student student = new Student();
    student.setId("test-student-id");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setAge(36);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> studentList = sut.search();
    Student actual = sut.searchStudent("test-student-id");

    assertThat(studentList).hasSize(10);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo("test-student-id");
    assertThat(actual.getName()).isEqualTo("江並公史");
    assertThat(actual.getKanaName()).isEqualTo("エナミコウジ");
    assertThat(actual.getNickname()).isEqualTo("エナミ");
    assertThat(actual.getEmail()).isEqualTo("test@example.com");
    assertThat(actual.getArea()).isEqualTo("奈良県");
    assertThat(actual.getAge()).isEqualTo(36);
    assertThat(actual.getSex()).isEqualTo("男性");
  }

  @Test
  void 受講生コース情報の登録が行えること() {

    String studentId =
        "61e9a148-4fea-4735-8065-0539daa9f3fc";

    LocalDateTime courseStartAt =
        LocalDateTime.of(2026, 8, 1, 9, 0);

    LocalDateTime courseEndAt =
        LocalDateTime.of(2027, 8, 1, 9, 0);

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("test-course-id");
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName("テストコース");
    studentCourse.setCourseStartAt(courseStartAt);
    studentCourse.setCourseEndAt(courseEndAt);

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> allCourses =
        sut.searchStudentCourseList();

    List<StudentCourse> actual =
        sut.searchStudentCourseListByStudentId(studentId);

    assertThat(allCourses).hasSize(10);

    assertThat(actual)
        .extracting(StudentCourse::getId)
        .contains("test-course-id");

    StudentCourse registeredCourse = actual.stream()
        .filter(course ->
            "test-course-id".equals(course.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(registeredCourse.getStudentId())
        .isEqualTo(studentId);

    assertThat(registeredCourse.getCourseName())
        .isEqualTo("テストコース");

    assertThat(registeredCourse.getCourseStartAt())
        .isEqualTo(courseStartAt);

    assertThat(registeredCourse.getCourseEndAt())
        .isEqualTo(courseEndAt);
  }

  @Test
  void 受講生情報の更新が行えること() {

    String studentId =
        "88c6624b-f736-4ed0-9729-b5476068ddab";

    Student student = sut.searchStudent(studentId);

    student.setName("中村 三郎");
    student.setKanaName("ナカムラ サブロウ");
    student.setNickname("さぶろうくん");
    student.setEmail("saburo@example.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男性");
    student.setRemark("Repository更新テスト");
    student.setDeleted(true);

    sut.updateStudent(student);

    Student actual = sut.searchStudent(studentId);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(studentId);
    assertThat(actual.getName()).isEqualTo("中村 三郎");
    assertThat(actual.getKanaName())
        .isEqualTo("ナカムラ サブロウ");
    assertThat(actual.getNickname())
        .isEqualTo("さぶろうくん");
    assertThat(actual.getEmail())
        .isEqualTo("saburo@example.com");
    assertThat(actual.getArea()).isEqualTo("東京都");
    assertThat(actual.getAge()).isEqualTo(30);
    assertThat(actual.getSex()).isEqualTo("男性");
    assertThat(actual.getRemark())
        .isEqualTo("Repository更新テスト");
  }

  @Test
  void 受講生コース情報の更新が行えること() {

    String courseId =
        "20e961da-5517-4d61-8a63-d85b3561bfdd";

    String studentId =
        "61e9a148-4fea-4735-8065-0539daa9f3fc";

    List<StudentCourse> courses =
        sut.searchStudentCourseListByStudentId(studentId);

    StudentCourse studentCourse = courses.stream()
        .filter(course ->
            courseId.equals(course.getId()))
        .findFirst()
        .orElseThrow();

    studentCourse.setCourseName("データベース応用");

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual =
        sut.searchStudentCourseListByStudentId(studentId);

    StudentCourse updatedCourse = actual.stream()
        .filter(course ->
            courseId.equals(course.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(updatedCourse.getId())
        .isEqualTo(courseId);

    assertThat(updatedCourse.getCourseName())
        .isEqualTo("データベース応用");
  }
}