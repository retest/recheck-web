package de.retest.web.testutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
@ExtendWith( SystemPropertyExtension.class )
public @interface SystemProperty {

	String key();

	String value() default "";

}
