package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生と受講生コースを受講生詳細に変換できること() {

    Student student =
        createStudent("student-id-1", "山田太郎");

    StudentCourse studentCourse =
        createStudentCourse(
            "course-id-1",
            "student-id-1",
            "Javaコース");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual =
        sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);

    StudentDetail studentDetail = actual.get(0);

    assertThat(studentDetail.getStudent())
        .isSameAs(student);

    assertThat(studentDetail.getStudentCourseList())
        .hasSize(1)
        .containsExactly(studentCourse);
  }

  @Test
  void 複数の受講生にそれぞれの受講生コースを紐づけて変換できること() {

    Student firstStudent =
        createStudent("student-id-1", "山田太郎");

    Student secondStudent =
        createStudent("student-id-2", "佐藤花子");

    StudentCourse firstCourse =
        createStudentCourse(
            "course-id-1",
            "student-id-1",
            "Javaコース");

    StudentCourse secondCourse =
        createStudentCourse(
            "course-id-2",
            "student-id-2",
            "AWSコース");

    List<Student> studentList =
        List.of(firstStudent, secondStudent);

    List<StudentCourse> studentCourseList =
        List.of(firstCourse, secondCourse);

    List<StudentDetail> actual =
        sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(2);

    assertThat(actual.get(0).getStudent())
        .isSameAs(firstStudent);

    assertThat(actual.get(0).getStudentCourseList())
        .containsExactly(firstCourse);

    assertThat(actual.get(1).getStudent())
        .isSameAs(secondStudent);

    assertThat(actual.get(1).getStudentCourseList())
        .containsExactly(secondCourse);
  }

  @Test
  void 受講生コースがない受講生は空のコースリストで変換されること() {

    Student student =
        createStudent("student-id-1", "山田太郎");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of();

    List<StudentDetail> actual =
        sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);

    StudentDetail studentDetail = actual.get(0);

    assertThat(studentDetail.getStudent())
        .isSameAs(student);

    assertThat(studentDetail.getStudentCourseList())
        .isEmpty();
  }

  @Test
  void 受講生に紐づかない受講生コースは除外されること() {

    Student student =
        createStudent("student-id-1", "山田太郎");

    StudentCourse unrelatedCourse =
        createStudentCourse(
            "course-id-1",
            "student-id-2",
            "Javaコース");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(unrelatedCourse);

    List<StudentDetail> actual =
        sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);

    StudentDetail studentDetail = actual.get(0);

    assertThat(studentDetail.getStudent())
        .isSameAs(student);

    assertThat(studentDetail.getStudentCourseList())
        .isEmpty();
  }

  private Student createStudent(String id, String name) {

    Student student = new Student();
    student.setId(id);
    student.setName(name);

    return student;
  }

  private StudentCourse createStudentCourse(
      String id,
      String studentId,
      String courseName) {

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(id);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName(courseName);

    return studentCourse;
  }
}