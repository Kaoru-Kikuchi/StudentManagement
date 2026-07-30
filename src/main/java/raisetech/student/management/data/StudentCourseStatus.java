package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コース申込状況を扱うクラスです。
 */
@Getter
@Setter
public class StudentCourseStatus {

  private String id;
  private String studentCourseId;
  private String status;
}