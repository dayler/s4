/**
 * 
 */
package com.truextend.s4;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.truextend.s4.exception.ApplicationExceptionMapper;
import com.truextend.s4.ws.Courses;
import com.truextend.s4.ws.Students;

/**
 * S4 application configuration for RESful Jersey.
 * 
 * @author arielsalazar
 */
@ApplicationPath("/ws")
public class S4App extends ResourceConfig {

    /**
     * S4App's constructor.
     */
    public S4App() {
        // register resources.
        register(Students.class);
        register(Courses.class);
        // register exception mapper.
        register(ApplicationExceptionMapper.class);
    }
}
