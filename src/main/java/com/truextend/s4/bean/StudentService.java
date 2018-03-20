/**
 * 
 */
package com.truextend.s4.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.truextend.s4.domain.Student;
import com.truextend.s4.exception.DuplicateEntryException;
import com.truextend.s4.exception.NotFoundEntryException;
import com.truextend.s4.utils.Csv;
import com.truextend.s4.utils.Strings;

/**
 * Manage all operations with students. create, delete, update, etc.
 * 
 * @author arielsalazar
 */
public class StudentService {

    private static Logger logger = Logger.getLogger(StudentService.class.getName());

    /**
     * Students data map.
     */
    private Map<Integer, Student> studentMap;

    /**
     * Load all students to the map.
     */
    public void init() {
        try {
            // read and fill students.
            List<String[]>lines = Csv.read(StudentService.class.getResourceAsStream("/studentsdata.csv"), 0, "#");
            studentMap = lines.stream()
                              .map(StudentService::studentFromMetadata)
                              .filter(Objects::nonNull)
                              .collect(Collectors.toMap(Student::getId, s -> s, (st0, st1) -> st0));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to get students...", ex);
        }
    }
    
    /**
     * For test purpose. Setter for the studentMap.
     * @param studentMap
     */
    public void setStudentMap(Map<Integer, Student> studentMap) {
        this.studentMap = new HashMap<>(studentMap);
    }
    
    /**
     * @param metadata
     * @return Builds an student from its metadata.
     */
    static Student studentFromMetadata(String[] metadata) {
        try {
            return new Student(Integer.valueOf(metadata[0].trim()), metadata[1].trim(), metadata[2].trim());
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Failed to create Student from metadata:" + Strings.join(metadata), ex);
            return null;
        }
    }

    /**
     * 
     * @param studentId Student identifier.
     * @return An Student to corresponds with the studentId.
     * @throws NotFoundEntryException If does not have an student for <code>studentId</code>.
     */
    public Student getStudentById(Integer studentId) throws NotFoundEntryException {
        Objects.requireNonNull(studentId, "null studentId.");
        // 
        return Optional.ofNullable(studentMap.get(studentId)).orElseThrow(() -> new NotFoundEntryException("student:" + studentId));
    }
    
    /**
     * 
     * @param studentId
     * @return An Student to corresponds with the studentId, if it does not exists returns <code>null</code>.
     */
    public Student uncheckedStudentById(Integer studentId) {
        return studentMap.get(studentId);
    }
    
    /**
     * Delete an specific student.
     * @param studentId Student identifier.
     */
    public void deleteStudent(Integer studentId) {
        Objects.requireNonNull(studentId, "null studentId.");
        // 
        studentMap.remove(studentId);
    }

    /**
     * Create insert an student.
     * @param student 
     * @throws DuplicateEntryException If the student id is duplicated.
     */
    public void createStudent(Student student) throws DuplicateEntryException {
        Objects.requireNonNull(student, "null student.");
        // 
        Student old = studentMap.get(student.getId());
        if (old ==  null) {
            studentMap.put(student.getId(), student);
            return;
        }
        throw new DuplicateEntryException("Student:" + student.getId());
    }

    /**
     * @return All registered students.
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    /**
     * @param firstName
     * @param lastName
     * @return All students that match with first name or the last name.
     */
    public List<Student> find(String firstName, String lastName) {
        return studentMap.values().stream()
                                  .filter(st -> Strings.contains(firstName, st.getFirstName()) || Strings.contains(lastName, st.getLastName()))
                                  .collect(Collectors.toList());
    }

    /**
     * Update an specific student. Not all fields of an student can be modified, the fields able to update are: first name and last name.
     * @param studentId
     * @param firstName
     * @param lastName
     * @throws NotFoundEntryException If does not have an student for <code>studentId</code>.
     */
    public void updateStudent(int studentId, String firstName, String lastName) throws NotFoundEntryException {
        // 
        Student student = studentMap.get(studentId);
        if (student == null) {
            // not found entry.
            throw new NotFoundEntryException("Student:" + studentId);
        }
        // update student.
        if (Objects.nonNull(firstName) && !Strings.equals(firstName, student.getFirstName())) {
            // update field.
            student.setFirstName(firstName);
        }
        if (Objects.nonNull(lastName) && !Strings.equals(lastName, student.getLastName())) {
            // update field.
            student.setLastName(lastName);
        }
    }

    /**
     * Enroll the student to an course.
     * @param courseCode Course identifier.
     * @param studentId Student identifier.
     * @throws NotFoundEntryException If student or course does not exists.
     */
    public void enroll(String courseCode, Integer studentId) throws NotFoundEntryException {
        Objects.requireNonNull(courseCode, "null courseCode.");
        Objects.requireNonNull(studentId, "null studentId.");
        // 
        Student student = Optional.ofNullable(studentMap.get(studentId)).orElseThrow(() -> new NotFoundEntryException("Student:" + studentId));
        student.takeCourse(courseCode);
    }
}
