/**
 * 
 */
package com.truextend.s4.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Define the Course structure.
 * @author arielsalazar
 */
@XmlRootElement(name="class")
@XmlAccessorType(XmlAccessType.FIELD)
public class Course {
    
    @XmlElement
    private String code;
    
    @XmlElement
    private String title;
    
    @XmlElement
    private String description;

    @XmlTransient
    private Set<Integer>studentList = new HashSet<>();
    
    public Course() {
        // No op.
    }
    
    public Course(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Set<Integer> getStudentList() {
        return Collections.unmodifiableSet(studentList);
    }
    
    public void addStudent(int studentId) {
        studentList.add(studentId);
    }
    
    @Override
    public String toString() {
        return String.format("%s %s %s", code, title, description);
    }
}
