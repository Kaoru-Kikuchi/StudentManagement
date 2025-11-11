package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.CourseRegistrationStatus;

@Mapper
public interface CourseRegistrationStatusRepository {

  @Select("SELECT * FROM course_registration_status WHERE student_course_id = #{studentCourseId}")
  List<CourseRegistrationStatus> findByStudentCourseId(
      @Param("studentCourseId") int studentCourseId);

  @Insert("INSERT INTO course_registration_status(student_course_id, registration_status) VALUES(#{studentCourseId}, #{registrationStatus})")
  void insert(CourseRegistrationStatus status);

  @Update("UPDATE course_registration_status SET registration_status = #{registrationStatus} WHERE id = #{id}")
  void update(CourseRegistrationStatus status);
}
