package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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

    Student student = new Student();
    student.setId("student-id-1");
    student.setName("山田太郎");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("course-id-1");
    studentCourse.setStudentId("student-id-1");
    studentCourse.setCourseName("Javaコース");

    List<Student> studentList = new ArrayList<>();
    studentList.add(student);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);

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

    Student firstStudent = new Student();
    firstStudent.setId("student-id-1");
    firstStudent.setName("山田太郎");

    Student secondStudent = new Student();
    secondStudent.setId("student-id-2");
    secondStudent.setName("佐藤花子");

    StudentCourse firstCourse = new StudentCourse();
    firstCourse.setId("course-id-1");
    firstCourse.setStudentId("student-id-1");
    firstCourse.setCourseName("Javaコース");

    StudentCourse secondCourse = new StudentCourse();
    secondCourse.setId("course-id-2");
    secondCourse.setStudentId("student-id-2");
    secondCourse.setCourseName("AWSコース");

    List<Student> studentList = new ArrayList<>();
    studentList.add(firstStudent);
    studentList.add(secondStudent);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(firstCourse);
    studentCourseList.add(secondCourse);

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

    Student student = new Student();
    student.setId("student-id-1");
    student.setName("山田太郎");

    List<Student> studentList = new ArrayList<>();
    studentList.add(student);

    List<StudentCourse> studentCourseList = new ArrayList<>();

    List<StudentDetail> actual =
        sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).hasSize(1);

    StudentDetail studentDetail = actual.get(0);

    assertThat(studentDetail.getStudent())
        .isSameAs(student);

    assertThat(studentDetail.getStudentCourseList())
        .isEmpty();
  }
}