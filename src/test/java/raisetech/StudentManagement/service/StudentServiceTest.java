package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void befor() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    //Mock化
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    //実行
    sut.searchStudentList();

    //検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細検索_IDに紐づく受講生情報と受講生コース情報を取得できること() {
    //事前準備
    Student student = new Student();
    student.setId("testId");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchStudent("testId")).thenReturn(student);
    when(repository.searchStudentCourse("testId")).thenReturn(studentCourseList);

    //実行
    StudentDetail actual = sut.searchStudent("testId");

    //検証
    verify(repository, times(1)).searchStudent("testId");
    verify(repository, times(1)).searchStudentCourse("testId");
    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細の登録_受講生と受講生コース情報を個別に登録できること() {
    //事前準備
    Student student = new Student();
    student.setId("testId");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentDetail studentDetails = new StudentDetail(student, studentCourseList);

    //実行
    StudentDetail actual = sut.registerStudent(studentDetails);

    //検証
    verify(repository, times(1)).registerStudent(student);

    for (StudentCourse studentCourse : studentCourseList) {
      verify(repository, times(1)).registerStudentCourse(studentCourse);
    }
    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細の更新_受講生を更新できること() {
    //事前準備
    Student student = new Student();
    student.setId("testId");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentDetail studentDetails = new StudentDetail(student, studentCourseList);

    //実行
    sut.updateStudent(studentDetails);

    //検証
    verify(repository, times(1)).updateStudent(student);

    for (StudentCourse studentCourse : studentCourseList) {
      verify(repository, times(1)).updateStudentCourse(studentCourse);
    }
    Assertions.assertEquals(student, studentDetails.getStudent());
    Assertions.assertEquals(studentCourseList, studentDetails.getStudentCourseList());
  }

  @Test
  void 受講生詳細の更新_受講生と受講生コース情報それぞれを更新できること() {
    //事前準備
    Student student = new Student();
    student.setId("testId");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse course1 = new StudentCourse();
    studentCourseList.add(course1);
    StudentDetail studentDetails = new StudentDetail(student, studentCourseList);

    //実行
    sut.updateStudent(studentDetails);

    //検証
    verify(repository, times(1)).updateStudent(student);

    for (StudentCourse studentCourse : studentCourseList) {
      verify(repository, times(1)).updateStudentCourse(studentCourse);
    }
    Assertions.assertEquals(student, studentDetails.getStudent());
    Assertions.assertEquals(studentCourseList, studentDetails.getStudentCourseList());
  }
}
