package raisetech.StudentManagement.data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegistrationStatus {

  private Integer id; // AUTO_INCREMENT
  private Integer studentCourseId; // students_courses の id
  private String registrationStatus; // 仮申込・本申込・受講中・受講終了
}

