package in.precisiontestautomation.annotations;

import in.precisiontestautomation.enums.FileTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReadFile {

    String directoryPath() default "";
    String fileNameStartsWith() default "";
    FileTypes fileType();
    int timeOut() default 10;
}
