package louiseeo.model;

/**
 * This class is used to represent a Grades object with its name and grades for
 * prelim, midterm, and finals.
 * 
 * @author Louiseeo
 */
    public class Grades {
    private String subject;
    private double prelim;
    private double midterm;
    private double finals;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getPrelim() {
        return prelim;
    }

    public void setPrelim(double prelim) {
        this.prelim = prelim;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getFinals() {
        return finals;
    }

    public void setFinals(double finals) {
        this.finals = finals;
    }

    public Grades(String subject, double prelim, double midterm, double finals) {
        this.subject = subject;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finals = finals;
    }

    public Grades() {} // parameterless constructor

    /**
     * This overrides the method toString() to return a user-friendly object
     * representation
     * 
     * @return the user-friendly string format of the object
     */
    @Override
    public String toString() {
        return String.format("""
                Subject: %s
                Prelims: %f
                Midterms: %f
                Finals: %f
                """, subject, prelim, midterm, finals);
    }

}
