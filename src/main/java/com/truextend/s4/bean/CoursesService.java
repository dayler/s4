/**
 * 
 */
package com.truextend.s4.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.truextend.s4.domain.Course;
import com.truextend.s4.exception.DuplicateEntryException;
import com.truextend.s4.exception.NotFoundEntryException;
import com.truextend.s4.utils.Csv;
import com.truextend.s4.utils.Strings;

/**
 * Manage all operations with <code>Course</code>s, create, delete, update, etc.
 * @author arielsalazar
 *
 */
public class CoursesService {
    
    private static Logger logger = Logger.getLogger(CoursesService.class.getName());
    
    /**
     * Course map. Contains all data for courses.
     */
    private Map<String, Course>courseMap;
    
    /**
     * Student controller. Used to enroll the students.
     */
    @Autowired
    private StudentService studentService;
    
    /**
     * Initializer method, load all data and initialize all services.
     */
    public void init() {
        // read and fill courses.
        try {
            List<String[]>lines = Csv.read(CoursesService.class.getResourceAsStream("/coursesdata.csv"), 0, "#");
            courseMap = lines.stream()
                             .map(this::courseFomrMetadata)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toMap(Course::getCode, course -> course, (course0, course1) -> course0));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to get courses...", ex);
        }
    }
    
    /**
     * For test purpose. Setter for courseMap.
     * @param courseMap
     */
    protected void setCourseMap(Map<String, Course> courseMap) {
        this.courseMap = new HashMap<>(courseMap);
    }
    
    /**
     * For test purpose. Setter for studentController.
     * @param studentController
     */
    protected void setStudentService(StudentService studentController) {
        this.studentService = studentController;
    }
    
    /**
     * @param metadata
     * @return Creates a <code>Course</code> from its metadata.
     */
    protected Course courseFomrMetadata(String[] metadata) {
        try {
            Course course = new Course(metadata[0].trim(), metadata[1].trim(), metadata[2].trim());
            // read students.
            Set<Integer> studentIdList = null;
            studentIdList = Arrays.stream(metadata[3].trim().split("\\|")).map(Integer::valueOf)
                    .collect(Collectors.toSet());
            // register student in the course.
            for (Integer stId : studentIdList) {
                try {
                    studentService.enroll(course.getCode(), stId);
                    course.addStudent(stId);
                } catch (NotFoundEntryException ex) {
                    logger.log(Level.WARNING, "Failed to enroll student:" + stId, ex);
                }
            }
            return course;
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Failed to create Course from metadata:" + Strings.join(metadata), ex);
            return null;
        }
    }
    
    /**
     * @param code Identifier of a <code>Course</code>.
     * @return The course that corresponds with <code>code</code>.
     * @throws NotFoundEntryException If the Course with the code does not exists.
     */
    public Course getCourseByCode(String code) throws NotFoundEntryException {
        Objects.requireNonNull(code, "null code");
        // 
        return Optional.ofNullable(courseMap.get(code)).orElseThrow(() -> new NotFoundEntryException("class:" + code));
    }
    
    /**
     * @param code
     * @return The course that corresponds with <code>code</code>, if the course does not exists returns <code>null</code>.
     */
    public Course uncheckedCourseByCode(String code) {
        return courseMap.get(code);
    } 
    
    /**
     * Delete an specific <code>Course</code>.
     * @param code Course identifier.
     */
    public void deleteCourse(String code) {
        Objects.requireNonNull(code, "null code.");
        // 
        courseMap.remove(code);
    }

    /**
     * @return All available courses.
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    public List<Course> find(String title, String description) {
        return courseMap.values()
                        .stream()
                        .filter(course -> Strings.contains(title, course.getTitle()) || Strings.contains(description, course.getDescription()))
                        .collect(Collectors.toList());
    }

    /**
     * Updates a course. Not all parameters can be update, the parameters available to update are: title and description.
     * @param code Course identifier.
     * @param title
     * @param description
     * @throws NotFoundEntryException If the Course with the code does not exists.
     */
    public void updateCourse(String code, String title, String description) throws NotFoundEntryException {
        Course course = courseMap.get(code);
        if (course == null) {
            // not found entry.
            throw new NotFoundEntryException("Class:" + code);
        }
        // update course.
        if (!Strings.equals(title, course.getTitle())) {
            // update field.
            course.setTitle(title);
        }
        if (!Strings.equals(description, course.getDescription())) {
            // update field.
            course.setDescription(description);
        }
    }
    
    /**
     * Enroll an Student to an specific course.
     * @param code Course identifier.
     * @param studentId
     * @throws NotFoundEntryException If the Course or Student does not exists.
     */
    public void enrollStudent(String code, Integer studentId) throws NotFoundEntryException {
        Objects.requireNonNull(code, "null code");
        Objects.requireNonNull(studentId, "studentId");
        // 
        Course course = Optional.ofNullable(courseMap.get(code)).orElseThrow(() -> new NotFoundEntryException("class:" + code));
        // do enroll.
        studentService.enroll(code, studentId);
        course.addStudent(studentId);
    }

    /**
     * Creates a new Course.
     * @param course
     * @throws DuplicateEntryException If the code for the course is duplicated.
     */
    public void createCourse(Course course) throws DuplicateEntryException {
        Objects.requireNonNull(course, "course");
        // 
        Course old = courseMap.get(course.getCode());
        if (old == null) {
            courseMap.put(course.getCode(), course);
            return;
        }
        throw new DuplicateEntryException("Class:" + course.getCode());
    }
}
