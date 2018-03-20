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
 * End point service, this class implements all "students" services provided by the app. The root context /ws/students
 * @author arielsalazar
 */
@Path("students")
public class Students {

    /**
     * Student controller.
     */
    @Autowired
    private StudentService studentService;
    
    /**
     * Courses controller.
     */
    @Autowired
    private CoursesService coursesService;
    
    /**
     * @param studentId
     * @return Student to corresponds with studentId.
     * @throws NotFoundEntryException If the <code>Student</code> does not exists.
     */
    @GET
    @Path("{studentId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("studentId") Integer studentId) throws NotFoundEntryException {
        return studentService.getStudentById(studentId);
    }

    /**
     * Delete a Student to corresponds with studentId.
     * @param studentId
     */
    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteStudent(@FormParam("studentId") Integer studentId) {
        studentService.deleteStudent(studentId);
        return Response.status(Response.Status.OK)
                       .type(MediaType.APPLICATION_JSON)
                       .entity(studentId)
                       .build();
    }

    /**
     * Creates an <code>Student</code>.
     * @param student
     * @return
     * @throws DuplicateEntryException if the <code>Student Id</code> is duplicated.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createStudent(@FormParam("student") Student student) throws DuplicateEntryException {
        studentService.createStudent(student);
        return Response.status(Response.Status.CREATED)
                       .type(MediaType.APPLICATION_JSON)
                       .entity(student)
                       .build();
    }

    /**
     * @return All <code>Student</code>s subscribed in the app.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> getAllStudents() {
        try {
        return studentService.getAllStudents();
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * @param firstName
     * @param lastName
     * @return All Students to match with "firstName" or "lastName".
     */
    @GET
    @Path("find")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> find(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        return studentService.find(firstName, lastName);
    }

    /**
     * Update the following fields for an Student: first name and last name.
     * @param studentId
     * @param firstName
     * @param lastName
     * @throws NotFoundEntryException If the Student does not exists.
     */
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateStudent(@FormParam("student") Student student) throws NotFoundEntryException {
        studentService.updateStudent(student.getId(), student.getFirstName(), student.getLastName());
        return Response.status(Response.Status.CREATED)
                                       .type(MediaType.APPLICATION_JSON)
                                       .entity(student)
                                       .build();
    }
    
    /**
     * @param studentId
     * @return All Courses in is enrolled the Student.
     * @throws NotFoundEntryException If the Student does not exists.
     */
    @GET
    @Path("{studentId}/classes")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> getCoursesbyStudent(@PathParam("studentId") Integer studentId) throws NotFoundEntryException {
        Student student = studentService.getStudentById(studentId);
        return student.getCourseList().stream().map(coursesService::uncheckedCourseByCode).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
