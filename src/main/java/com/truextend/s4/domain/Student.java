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
 * Define the Student structure.
 * @author arielsalazar
 */
@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {
    
    @XmlElement(name="studentId")
    private int id;
    
    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;
    
    @XmlTransient
    private Set<String>courseList;
    
    public Student() {
        // No op.
    }
    
    public Student(int id, String firstName, String lastName) {
        this(id, firstName, lastName, new HashSet<>());
    }
    
    public Student(int id, String firstName, String lastName, Set<String>courseList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        // defensive copy.
        this.courseList = courseList == null ? new HashSet<>() : new HashSet<>(courseList);
    }
    
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Set<String> getCourseList() {
        return Collections.unmodifiableSet(courseList);
    }
    
    public void takeCourse(String code) {
        courseList.add(code);
    }
    
    @Override
    public String toString() {
        return String.format("%s %s %s", id, firstName, lastName);
    }
}
