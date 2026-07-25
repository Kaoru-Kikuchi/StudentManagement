package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コーステーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   * @return　受講生一覧（全件）
   */
  @Select("SELECT * FROM students")
  List<Student> search();

  /**
   * 受講生の検索を行います
   * @param id　受講生ID
   * @return　受講生
   */
  @Select("SELECT * FROM students WHERE id =#{id}")
  Student searchStudent(String id);

  /**
   * 受講生のコース情報全件検索を行います。
   * @return　受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生のコース情報を検索します。
   * @param studentId　受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id =#{studentId}")
 List<StudentCourse> searchStudentCourses(String studentId);

  @Insert("""
    INSERT INTO students (
      id,
      name,
      kana_name,
      nickname,
      email,
      area,
      age,
      sex,
      remark,
      is_deleted
    )
    VALUES (
      #{id},
      #{name},
      #{kanaName},
      #{nickname},
      #{email},
      #{area},
      #{age},
      #{sex},
      #{remark},
      false
    )
    """)
  void registerStudent(Student student);

  @Insert("""
    INSERT INTO students_courses (
      id,
      student_id,
      course_name,
      course_start_at,
      course_end_at
    )
    VALUES (
      #{id},
      #{studentId},
      #{courseName},
      #{courseStartAt},
      #{courseEndAt}
    )
    """)
  void registerStudentCourse(StudentCourse studentCourse);

  @Update("""
      UPDATE students
      SET
        name = #{name},
        kana_name = #{kanaName},
        nickname = #{nickname},
        email = #{email},
        area = #{area},
        age = #{age},
        sex = #{sex},
        remark = #{remark},
        is_deleted = #{isDeleted}
      WHERE id = #{id}
      """)
  void updateStudent(Student student);

  @Update("""
      UPDATE students_courses
      SET
        course_name = #{courseName}
      WHERE id = #{id}
      """)
  void updateStudentCourse(StudentCourse studentCourse);
}