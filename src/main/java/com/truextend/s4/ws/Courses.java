/**
 * 
 */
package com.truextend.s4.ws;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.truextend.s4.bean.CoursesService;
import com.truextend.s4.bean.StudentService;
import com.truextend.s4.domain.Course;
import com.truextend.s4.domain.Student;
import com.truextend.s4.exception.DuplicateEntryException;
import com.truextend.s4.exception.NotFoundEntryException;

/**
 * End point service, this class implements all "classes" services provided by the app. The root context /ws/classes
 * @author arielsalazar
 */
@Path("classes")
public class Courses {
    
    /**
     * Handle all operation on students.
     */
    @Autowired
    private StudentService studentService;
    
    /**
     * Handle all operations on courses.
     */
    @Autowired
    private CoursesService courseService;
    
    /**
     * @param code Course identifier.
     * @return The <code>Course</code> to corresponds with <code>code</code>.
     * @throws NotFoundEntryException if the <code>Course</code> does not exists.
     */
    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Course getCourse(@PathParam("code") String code) throws NotFoundEntryException {
        return courseService.getCourseByCode(code);
    }

    /**
     * Delete a <code>Course</code>.
     * @param code Course identifier.
     */
    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteCourse(@FormParam("code") String code) {
        courseService.deleteCourse(code);
        return Response.status(Response.Status.OK)
                       .type(MediaType.APPLICATION_JSON)
                       .entity("Deleted:" + code)
                       .build();
    }

    /**
     * Creates a new <code>Course</code>.
     * @param course
     * @throws DuplicateEntryException if the <code>code</code> (course identified) is duplicated.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createCourse(@FormParam("class") Course course) throws DuplicateEntryException {
        courseService.createCourse(course);
        return Response.status(Response.Status.CREATED)
                       .type(MediaType.APPLICATION_JSON)
                       .entity("Created:" + course)
                       .build();
    }

    /**
     * @return All <code>Course</code>s that already register in the app.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * @param title
     * @param description
     * @return Find all courses that match with "firstName" or "description". If it is sent null for any of those parameters, the condition is ignored.
     */
    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> find(@QueryParam("title") String title, @QueryParam("description") String description) {
        return courseService.find(title, description);
    }

    /**
     * Updates the following fields for an Course: tile and description.
     * @param code
     * @param title
     * @param description
     * @return
     * @throws NotFoundEntryException If a Course for the <code>code</code> does not exists.
     */
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCourse(Course course) throws NotFoundEntryException {
        courseService.updateCourse(course.getCode(), course.getTitle(), course.getDescription());
      return Response.status(Response.Status.CREATED)
                     .type(MediaType.APPLICATION_JSON)
                     .entity("Updated:" + course.getCode())
                     .build();
    }

    /**
     * @param code
     * @return The list of Students that are enrolled to <code>Course</code>.
     * @throws NotFoundEntryException
     */
    @GET
    @Path("{code}/students")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> getStudentsbyCourse(@PathParam("code") String code) throws NotFoundEntryException {
        Course course = courseService.getCourseByCode(code);
        return course.getStudentList().stream().map(studentService::uncheckedStudentById).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
