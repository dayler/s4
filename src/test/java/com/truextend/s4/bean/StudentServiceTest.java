/**
 * 
 */
package com.truextend.s4.bean;

import static org.junit.Assert.*;

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

import com.truextend.s4.domain.Student;
import com.truextend.s4.exception.DuplicateEntryException;
import com.truextend.s4.exception.NotFoundEntryException;
import com.truextend.s4.utils.Csv;

/**
 * StudentController's test class.
 * 
 * @author arielsalazar
 *
 */
public class StudentServiceTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private StudentService controller;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        controller = new StudentService();
        controller.setStudentMap(makeStudentMap());
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        controller = null;
    }

    /**
     * Test method for an un-existing student.
     * Test method for {@link com.truextend.s4.bean.StudentService#getStudentById(java.lang.Integer)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void getStudentByIdNotFoundEntryException() throws NotFoundEntryException {
        exception.expect(NotFoundEntryException.class);
        controller.getStudentById(77); // get un-existing student.
    }
    
    @Test
    public void getStudentById() throws NotFoundEntryException {
        Student student = controller.getStudentById(2); // get data for student 1.
        assertNotNull("null student.", student);
        assertEquals("At check first name.", "Leyla", student.getFirstName());
        assertEquals("At check last name.", "Hinojosa", student.getLastName());
    }
    
    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#uncheckedStudentById(java.lang.Integer)}.
     */
    @Test
    public void uncheckedStudentById() {
        // try get an student.
        Student student = controller.uncheckedStudentById(2); // get data for student 1.
        assertNotNull("null student.", student);
        assertEquals("At check first name.", "Leyla", student.getFirstName());
        assertEquals("At check last name.", "Hinojosa", student.getLastName());
        // null if the student is not registered.
        student =  controller.uncheckedStudentById(77);
        assertNull("Not null student.", student);
    }

    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#deleteStudent(java.lang.Integer)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void deleteStudent() throws NotFoundEntryException {
        Student student = controller.getStudentById(1);
        assertNotNull("null student.", student);
        // delete student.
        controller.deleteStudent(1);
        exception.expect(NotFoundEntryException.class);
        student = controller.getStudentById(1);
    }

    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#createStudent(com.truextend.s4.domain.Student)}.
     * @throws DuplicateEntryException 
     * @throws NotFoundEntryException 
     */
    @Test
    public void createStudent() throws DuplicateEntryException, NotFoundEntryException {
        Student newStudent = new Student(88, "Daniela", "Solvy");
        controller.createStudent(newStudent);
        Student student = controller.getStudentById(88);
        assertEquals("Failed to check student id.", newStudent.getId(), student.getId());
        assertEquals("Failed to check first name.", newStudent.getFirstName(), student.getFirstName());
        assertEquals("Failed to check last name.", newStudent.getLastName(), student.getLastName());
    }

    /**
     * Test create student with a duplicate student id.
     * @throws DuplicateEntryException
     * @throws NotFoundEntryException 
     */
    @Test
    public void createStudentDuplicateEntryException() throws DuplicateEntryException, NotFoundEntryException {
        Student student = controller.getStudentById(2);
        assertNotNull("null student.", student);
        Student duplicate = new Student(2, "Zarela", "Roso");
        exception.expect(DuplicateEntryException.class);
        controller.createStudent(duplicate);
    }
    
    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#getAllStudents()}.
     */
    @Test
    public void getAllStudents() {
        List<Student>studentList = controller.getAllStudents();
        assertNotNull("null student list.", studentList);
        assertFalse("Empty list", studentList.isEmpty());
    }

    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#find(java.lang.String, java.lang.String)}.
     */
    @Test
    public void find() {
        // find by last name.
        List<Student>students = controller.find(null, "Hinojosa");
        assertNotNull("null students.", students);
        assertEquals("At check size.", 3, students.size());
        Integer[]studentIds = new Integer[students.size()];
        students.stream().map(Student::getId).collect(Collectors.toList()).toArray(studentIds);
        assertArrayEquals("at check array.", new Integer[] {2, 16, 17}, studentIds);
        // find by first name.
        students = controller.find("Daniela", null);
        assertNotNull("null students.", students);
        assertEquals("At check size.", 2, students.size());
        studentIds = new Integer[students.size()];
        students.stream().map(Student::getId).collect(Collectors.toList()).toArray(studentIds);
        assertArrayEquals("at check array.", new Integer[] {4, 16}, studentIds);
    }

    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#updateStudent(java.lang.Integer, java.lang.String, java.lang.String)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void updateStudent() throws NotFoundEntryException {
        Student student = controller.getStudentById(3);
        assertNotNull("null student.", student);
        assertEquals("Cavero", student.getLastName());
        // update
        controller.updateStudent(3, null, "Cabrera");
        student = controller.getStudentById(3);
        assertEquals("Cabrera", student.getLastName());
    }

    /**
     * Test method for {@link com.truextend.s4.bean.StudentService#enroll(java.lang.String, java.lang.Integer)}.
     * @throws NotFoundEntryException 
     */
    @Test
    public void enroll() throws NotFoundEntryException {
        Student student = controller.getStudentById(1);
        assertNotNull("null student", student);
        assertTrue("not empty list", student.getCourseList().isEmpty());
        // enroll.
        controller.enroll("BMS301", 1);
        student = controller.getStudentById(1);
        assertFalse("empty list", student.getCourseList().isEmpty());
    }

    /**
     * Load test data for students.
     * @throws IOException 
     */
    private static Map<Integer, Student> makeStudentMap() throws IOException {
            // read and fill students.
            List<String[]>lines = Csv.read(StudentServiceTest.class.getResourceAsStream("/test_studentsdata.csv"), 0, "#");
            return lines.stream()
                        .map(StudentService::studentFromMetadata)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(Student::getId, s -> s, (st0, st1) -> st0));
    }

}
