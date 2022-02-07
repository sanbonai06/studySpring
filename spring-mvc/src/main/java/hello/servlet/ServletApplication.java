package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ServletComponentScan	//서블릿 자동 등록(스프링 컴포넌트 스캔이랑 비슷하게 동작)
@SpringBootApplication
public class ServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}

//	@Bean
//	InternalResourceViewResolver internalResourceViewResolver() {
//		return new InternalResourceViewResolver("/WEB-INF/views", ".jsp");
//	}
}
