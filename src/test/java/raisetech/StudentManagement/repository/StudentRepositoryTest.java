package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生をIDで検索できること() {
    Student actual = sut.searchStudent(1); // ← int に変更
    assertThat(actual).isNotNull();
    assertThat(actual.getName()).isEqualTo("山田太郎");
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
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

    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生IDでコース情報を検索できること() {
    List<StudentCourse> actual = sut.searchStudentCourse(1); // ← int に変更
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.get(0).getStudentId()).isEqualTo(1); // ← int 比較に変更
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId(1); // ← int に変更
    course.setCourseName("Pythonコース");
    course.setCourseStartAt(LocalDateTime.of(2025, 10, 22, 9, 0));
    course.setCourseEndAt(LocalDateTime.of(2025, 12, 22, 17, 0));

    sut.registerStudentCourse(course);
    assertThat(course.getId()).isNotNull();

    List<StudentCourse> actual = sut.searchStudentCourse(1);
    assertThat(actual.stream()
        .anyMatch(c -> "Pythonコース".equals(c.getCourseName())))
        .isTrue();
  }

  @Test
  void 受講生情報の更新が行えること() {
    Student student = sut.searchStudent(1);
    String oldName = student.getName();
    student.setName("更新後の名前");

    sut.updateStudent(student);

    Student updated = sut.searchStudent(1);
    assertThat(updated.getName()).isEqualTo("更新後の名前");
    assertThat(updated.getName()).isNotEqualTo(oldName);
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    List<StudentCourse> courses = sut.searchStudentCourse(1);
    StudentCourse course = courses.get(0);
    String oldCourseName = course.getCourseName();
    course.setCourseName("更新後コース");

    sut.updateStudentCourse(course);

    List<StudentCourse> updatedCourses = sut.searchStudentCourse(1);
    assertThat(updatedCourses.get(0).getCourseName()).isEqualTo("更新後コース");
    assertThat(updatedCourses.get(0).getCourseName()).isNotEqualTo(oldCourseName);
  }
}
