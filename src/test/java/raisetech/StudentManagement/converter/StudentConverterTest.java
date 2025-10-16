package raisetech.StudentManagement.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter converter;

  @BeforeEach
  void setUp() {
    converter = new StudentConverter();
  }

  @Test
  void 受講生とコースが正しく紐づく() {
    //準備
    Student student = new Student();
    student.setId("1");
    student.setName("Taro");

    StudentCourse course1 = new StudentCourse();
    course1.setStudentId("1");
    course1.setCourseName("Javaコース");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentId("1");
    course2.setCourseName("Spring Boot入門");

    List<Student> students = List.of(student);
    List<StudentCourse> courses = List.of(course1, course2);

    //実行
    List<StudentDetail> result = converter.convertStudentDetails(students, courses);

    // 検証
    assertEquals(1, result.size(), "受講生は一人のはず");
    assertEquals(2, result.get(0).getStudentCourseList().size(), "コースが2つ紐づくはず");
    assertEquals("Taro", result.get(0).getStudent().getName());
  }
}