public class Subject {
    // Make private fields
    private String subjectName;
    private double prelims;
    private double midterms;
    private double finals;
    

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getPrelims() {
        return prelims;
    }

    public void setPrelims(double prelims) {
        this.prelims = prelims;
    }

    public double getMidterms() {
        return midterms;
    }

    public void setMidterms(double midterms) {
        this.midterms = midterms;
    }

    public double getFinals() {
        return finals;
    }

    public void setFinals(double finals) {
        this.finals = finals;
    }

    public Subject(String subjectName, double prelims, double midterms, double finals) {
        this.subjectName = subjectName;
        this.prelims = prelims;
        this.midterms = midterms;
        this.finals = finals;
    }

    public Subject(){} // parameterless constructor

    // Add overriding
    @Override
    public String toString(){
        return String.format("""
                Subject: %s
                Prelims: %f
                Midterms: %f
                Finals: %f
                """, subjectName, prelims, midterms, finals);
    }
    
}
