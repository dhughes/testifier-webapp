package net.doughughes.testifier.entity;

import javax.persistence.*;

@Entity
public class TestException {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 2000000)
    private String message;
    @Column(length = 2000000)
    private String stackTrace;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(table = "test_exception", name = "caused_by_id")
    private TestException causedBy = null;

    public TestException() {
    }

    public TestException(Throwable exception) {
        if(exception != null) {
            this.message = exception.getMessage();
            StringBuilder stackTraceBuilder = new StringBuilder();

            for(StackTraceElement element : exception.getStackTrace()){
                stackTraceBuilder.append(element.toString() + "\r");
            }

            this.stackTrace = stackTraceBuilder.toString();

            if (exception.getCause() != null) {
                this.causedBy = new TestException(exception.getCause());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public TestException getCausedBy() {
        return causedBy;
    }

    public void setCausedBy(TestException causedBy) {
        this.causedBy = causedBy;
    }
}
