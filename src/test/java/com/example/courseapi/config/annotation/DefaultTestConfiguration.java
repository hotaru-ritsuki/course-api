package com.example.courseapi.config.annotation;

import com.example.courseapi.CourseApiApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseApiApplication.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface DefaultTestConfiguration {
}
