package net.doughughes.testifier.entity;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    private String studentName;
    private String studentEmail;
    private String projectName;
    private String className;
    private String methodName;

    private String unitTestName;
    private String testMethodName;

    private String result;

    @ElementCollection
    private List<String> arguments = new ArrayList<>();

    // this is stupid large because I looked for the longest java file on my laptop (1500000 lines) and used that as a guide
    @Column(length = 2000000)
    private String methodSource;
    @Column(length = 2000000)
    private String constructorSource;
    @Column(length = 2000000)
    private String classSource;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TestException exception = null;

    private String instructor;

    public Notification() {
    }

    public Notification(String studentName, String studentEmail, String projectName, String className, String methodName, Class[] arguments, String unitTestName, String testMethodName, String result, String methodSource, String constructorSource, String classSource, TestException exception, String instructor) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.projectName = projectName;
        this.className = className;
        this.methodName = methodName;
        this.unitTestName = unitTestName;
        this.testMethodName = testMethodName;
        this.result = result;
        this.methodSource = methodSource;
        this.constructorSource = constructorSource;
        this.classSource = classSource;
        this.exception = exception;
        this.instructor = instructor;

        // get the method arguments from the array of classes provided
        this.arguments = Arrays.stream(arguments).map(Class::getName).collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> methodArguments) {
        this.arguments = methodArguments;
    }

    public String getUnitTestName() {
        return unitTestName;
    }

    public void setUnitTestName(String unitTestName) {
        this.unitTestName = unitTestName;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMethodSource() {
        return methodSource;
    }

    public void setMethodSource(String methodSource) {
        this.methodSource = methodSource;
    }

    public TestException getException() {
        return exception;
    }

    public void setException(TestException exception) {
        this.exception = exception;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getConstructorSource() {
        return constructorSource;
    }

    public void setConstructorSource(String constructorSource) {
        this.constructorSource = constructorSource;
    }

    public String getClassSource() {
        return classSource;
    }

    public void setClassSource(String classSource) {
        this.classSource = classSource;
    }
}
