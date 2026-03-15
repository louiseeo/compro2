import java.util.ArrayList;
import java.util.List;

public class Student {
	// Create properties
	String name;
	List<Integer> attendanceMarks;	
	
	public Student(){
		attendanceMarks = new ArrayList<>(); // initialize to store attendance marks
	}
}
