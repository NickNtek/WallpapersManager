package com.example.wallpapermanager.Model;

public class ExceptionModel {

    private int id;
    private String message;
    private String stacktrace;
    private String logDate;
    private String className;

    public ExceptionModel(String message, String stacktrace, String logDate, String className) {
        this.message = message;
        this.stacktrace = stacktrace;
        this.logDate = logDate;
        this.className = className;
    }

    public ExceptionModel(int id, String message, String stacktrace, String logDate, String className) {
        this.id = id;
        this.message = message;
        this.stacktrace = stacktrace;
        this.logDate = logDate;
        this.className = className;
    }

    public ExceptionModel() {
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
