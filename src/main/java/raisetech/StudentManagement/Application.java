package raisetech.StudentManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info = @Info(
        title = "受講生管理システム　API",
        version = "0.0.1-SNAPSHOT",
        description = "受講生の登録・更新・検索を行うための API 仕様書です。",
        contact = @Contact(name = "システム管理者", email = "support@example.com"),
        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "ローカル開発用サーバー")
    }
)
@SpringBootApplication

public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}