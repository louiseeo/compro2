package com.louiseeo;

/**
 * Represents a subject grade entry containing prelim, midterm, and final grades.
 */
public class Grade {
    private String subject;
    private double prelim;
    private double midterm;
    private double finalGrade;

    public Grade(String subject, double prelim, double midterm, double finalGrade) {
        this.subject = subject;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finalGrade = finalGrade;
    }

    public String getSubject() {
        return subject;
    }

    public double getPrelim() {
        return prelim;
    }

    public double getMidterm() {
        return midterm;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPrelim(double prelim) {
        this.prelim = prelim;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }
}