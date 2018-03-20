/**
 * 
 */
package com.truextend.s4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.truextend.s4.bean.CoursesService;
import com.truextend.s4.bean.StudentService;

/**
 * Used to configure spring beans.
 * @author arielsalazar
 */
@Configuration
public class AppConfig {
    
    @Bean(initMethod="init")
    public CoursesService coursesController() {
        return new CoursesService();
    }
    
    @Bean(initMethod="init")
    public StudentService studentController() {
        return new StudentService();
    }
}
