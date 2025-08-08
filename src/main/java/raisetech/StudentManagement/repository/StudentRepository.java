package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;


@Mapper
public interface StudentRepository {

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
}