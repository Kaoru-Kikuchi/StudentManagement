package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;


@Mapper
public interface StudentRepository {


  @Select("SELECT IFNULL(MAX(CAST(id AS UNSIGNED)), 0) FROM students")
  int findMaxStudentId();

  @Select("SELECT IFNULL(MAX(CAST(id AS UNSIGNED)), 0) FROM students_courses")
  int findMaxStudentCourseId();


  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentsCourses();

  @Select("SELECT * FROM students WHERE isDeleted = #{id}")
  List<Student> findByIsDeleted();

  @Select("SELECT * FROM students WHERE remark IS NOT NULL")
  List<Student> findStudentWithRemark();

  @Update("UPDATE students SET IsDeleted = TRUE WHERE id = #{id}")
  int logicallyDelete(@Param("id") int id);

  @Insert("INSERT INTO students (id,name,kana_name,nickname,email,area,age,sex) VALUES (#{id},#{name},#{kanaName},#{nickname},#{email},#{area},#{age},#{sex})")
  void saveStudent(Student student);

  @Insert("INSERT INTO students_courses(id,student_id,course_name,course_start_at,course_end_at) " +
      "VALUES (#{id},#{studentId},#{courseName},#{courseStartAt},#{courseEndAt})")
  void saveStudentCourse(StudentCourse studentCourse);

}