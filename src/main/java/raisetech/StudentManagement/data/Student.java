package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  private int age;
  private String sex;
  private String remark;

  private boolean deleted;

  // JavaBeans仕様に沿った getter
  public boolean isDeleted() {
    return deleted;
  }

  // setter
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }


  @Override
  public String toString() {
    return "Student{name='" + name + "', age=" + age + "}";
  }
}

