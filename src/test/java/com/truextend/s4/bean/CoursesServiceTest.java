/**
 * 
 */
package com.truextend.s4.bean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.truextend.s4.domain.Course;
import com.truextend.s4.domain.Student;
import com.truextend.s4.exception.DuplicateEntryException;
import com.truextend.s4.exception.NotFoundEntryException;
import com.truextend.s4.utils.Csv;

/**
 * CoursesController's test class.
 * @author arielsalazar
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CoursesServiceTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CoursesService controller;
    
    @Mock
    private StudentService studentController;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        controller = new CoursesService();
        controller.setStudentService(studentController);
        controller.setCourseMap(makeCourseMap());
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        controller = null;
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#getCourseByCode(java.lang.String)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void getCourseByCode() throws NotFoundEntryException {
        Course course = controller.getCourseByCode("BMS301");
        assertNotNull("null course", course);
        assertEquals("At check title.", "Calculo 1", course.getTitle());
        assertEquals("At check description.", "Claculo", course.getDescription());
    }

    /**
     * Check exception throws if the course code is missing.
     * @throws NotFoundEntryException
     */
    @Test
    public void getCourseByCodeNotFoundException() throws NotFoundEntryException {
        exception.expect(NotFoundEntryException.class);
        controller.getCourseByCode("test");
    }
    
    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#uncheckedCourseByCode(java.lang.String)}.
     */
    @Test
    public void uncheckedCourseByCode() {
        // fin existed value.
        Course course = controller.uncheckedCourseByCode("INF135");
        assertNotNull("null course", course);
        // find un existed value.
        course = controller.uncheckedCourseByCode("NULL");
        assertNull("not nul course.", course);
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#deleteCourse(java.lang.String)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void deleteCourse() throws NotFoundEntryException {
        Course course = controller.getCourseByCode("FIS110");
        assertNotNull("nul course.", course);
        // delete course.
        controller.deleteCourse("FIS110");
        exception.expect(NotFoundEntryException.class);
        course = controller.getCourseByCode("FIS110");
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#getAllCourses()}.
     */
    @Test
    public void getAllCourses() {
        List<Course>courses = controller.getAllCourses();
        assertNotNull("null courses.", courses);
        assertFalse("empty list.", courses.isEmpty());
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#find(java.lang.String, java.lang.String)}.
     */
    @Test
    public void find() {
        // find by title.
        List<Course>courses = controller.find("Calculo", null);
        assertNotNull("null courses", courses);
        assertEquals("At check courses size.", 2, courses.size());
        // find by description.
        courses = controller.find(null, "Electricos");
        assertNotNull("null courses", courses);
        assertEquals("At check courses size.", 1, courses.size());
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#updateCourse(java.lang.String, java.lang.String, java.lang.String)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void updateCourse() throws NotFoundEntryException {
        Course course = controller.getCourseByCode("SRF301");
        assertEquals("Redes", course.getTitle());
        controller.updateCourse("SRF301", "Redes2", null);
        course = controller.getCourseByCode("SRF301");
        assertEquals("Redes2", course.getTitle());
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#enrollStudent(java.lang.String, java.lang.Integer)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void enrollStudent() throws NotFoundEntryException {
        // mock
        //doNothing().when(studentController).enroll(anyString(), anyInt());
        Student testStudent = new Student(1, "Ariel", "Salazar");
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String code = invocation.getArgument(0);
                testStudent.takeCourse(code);
                return null;
            }
        }).when(studentController).enroll(anyString(), anyInt());;
        when(studentController.getStudentById(1)).thenReturn(testStudent);
        // test
        controller.enrollStudent("FIS110", 1);
        Student student = studentController.getStudentById(1);
        assertEquals("FIS110", student.getCourseList().stream().filter(code -> "FIS110".equals(code)).findFirst().get());
        // verify
        verify(studentController, times(1)).getStudentById(1);
    }

    /**
     * Test method for {@link com.truextend.s4.bean.CoursesService#createCourse(com.truextend.s4.domain.Course)}.
     * @throws DuplicateEntryException 
     * @throws NotFoundEntryException 
     */
    @Test
    public void createCourse() throws DuplicateEntryException, NotFoundEntryException {
        Course newCourse = new Course("new", "Arte", "Bellas artes.");
        controller.createCourse(newCourse);
        Course course = controller.getCourseByCode("new");
        assertEquals("At check code.", newCourse.getCode(), course.getCode());
        assertEquals("At check title.", newCourse.getTitle(), course.getTitle());
        assertEquals("At check title.", newCourse.getDescription(), course.getDescription());
    }
    
    /**
     * Test exception is throwing when a duplicate code is trying to insert.
     * @throws DuplicateEntryException
     */
    @Test
    public void createCourse_duplicateEntryException() throws DuplicateEntryException {
        Course newCourse = new Course("SRF301", "Arte", "Bellas artes.");
        exception.expect(DuplicateEntryException.class);
        controller.createCourse(newCourse);
    }
    
    /**
     * Load data for courses.
     * @return
     * @throws IOException
     */
    private Map<String, Course> makeCourseMap() throws IOException {
        List<String[]> lines = Csv.read(CoursesService.class.getResourceAsStream("/test_coursesdata.csv"), 0, "#");
        return lines.stream()
                    .map(controller::courseFomrMetadata)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Course::getCode, course -> course, (course0, course1) -> course0));
    }
}
