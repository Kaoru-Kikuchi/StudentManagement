package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生詳細を受講生と受講生コース情報から組み立てるコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生と受講生コース情報を受講生詳細に変換します。
   *
   * @param studentList 受講生一覧
   * @param studentCourseList 受講生コース一覧
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentList,
      List<StudentCourse> studentCourseList) {

    List<StudentDetail> studentDetailList = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> studentCourseListByStudent =
          studentCourseList.stream()
              .filter(studentCourse ->
                  student.getId().equals(studentCourse.getStudentId()))
              .collect(Collectors.toList());

      studentDetail.setStudentCourseList(studentCourseListByStudent);
      studentDetailList.add(studentDetail);
    });

    return studentDetailList;
  }
}