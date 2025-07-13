package raisetech.StudentManagement;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private String name ;
	private String age ;
	private String address;
	private String school;
	private Map<String,String> infoMap = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}
	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		return "名前: " + name + "\n年齢: " + age + "歳\n住所: " + address + "\n学校: " + school;
	}

	@GetMapping("/studentInfoMap")
	public Map<String, String> getStudentInfoMap() {
		return infoMap;
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, String age, String address, String school) {
		if (StringUtils.isNotBlank(name)) {
			this.name = name;
			infoMap.put("name", name);
		}
		if (StringUtils.isNotBlank(age)) {
			this.age = age;
			infoMap.put("age", age);
		}
		if (StringUtils.isNotBlank(address)) {
			this.address = address;
			infoMap.put("address", address);
		}
		if (StringUtils.isNotBlank(school)) {
			this.school = school;
			infoMap.put("school", school);
		}
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name) {
		this.name = name;
		infoMap.put("name",name);
	}
	@PostMapping("/studentAge")
	public void updateStudentAge(String age) {
		this.age =age;
		infoMap.put("age",age);
	}
	@PostMapping("/studentAddress")
	public void updateStudentAddress(String address) {
		this.address = address;
		infoMap.put("address",address);
	}
	@PostMapping("/studentSchool")
	public void updateStudentSchool(String school) {
		this.school = school;
		infoMap.put("school",school);
	}
}
