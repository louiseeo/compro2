import java.util.ArrayList;
//import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class AttendanceApp {
	public static void main(String[] args) {
		ArrayList<Student> studentList = new ArrayList<>(); // initialize student arraylist

		// add students
		addStudent(studentList, "Sara Duterte");
		addStudent(studentList, "Leni Robredo");
		addStudent(studentList, "Sarah Discaya");

		// record their attendance
		recordAttendance(studentList, "Sara Duterte", 0);
		recordAttendance(studentList, "Sara Duterte", 0);
		recordAttendance(studentList, "Sara Duterte", 1);
		recordAttendance(studentList, "Leni Robredo", 1);
		recordAttendance(studentList, "Leni Robredo", 1);
		recordAttendance(studentList, "Leni Robredo", 1);
		recordAttendance(studentList, "Sarah Discaya", 1);
		recordAttendance(studentList, "Sarah Discaya", 1);
		recordAttendance(studentList, "Sarah Discaya", 0);

		recordAttendance(studentList, "Bongbong Marcos", 0); // record a non-existing student

		saveStudents(studentList, "attendance.txt"); // save students to text file

		ArrayList<Student> loadedStudents = loadStudents("attendance.txt"); // load records from file
		
		displayAllStudents(loadedStudents); // display all students

		loadStudents("data.txt"); // try a non-existing file for error handling
	}

	// Helper methods
	public static void addStudent(ArrayList<Student> students, String name) {
		Student st = new Student();
		st.name = name;
		students.add(st); // adds student to the arraylist
	}

	// Method that checks if student exist
	public static boolean recordAttendance(ArrayList<Student> students, String studentName, int mark) {
		for (Student s : students) {
			if (s.name.equalsIgnoreCase(studentName)) {
				s.attendanceMarks.add(mark);
				return true; // if successfully found
			}
		}
		System.out.println("Error: Student " + studentName + " does not exist!");
		return false; // if student is not found
	}

	// Method for finding the attendance percentage
	public static double getAttendancePercentage(Student student) {
		int total = student.attendanceMarks.size();
		int attended = 0; // initialize attended
		for (int mark : student.attendanceMarks) {
			if (mark == 1)
				attended += mark; // add all present marks(1)
		}
		return ((double) attended / total) * 100; // calculates and returns the percentage
	}

	// Method that returns representation of students
	public static String getDisplayInfo(Student student) {
		return ("Name: " + student.name + ", Attendance: " + String.format("%.2f", getAttendancePercentage(student)) + "%");
	}

	// Method that will display the overall info of students
	public static void displayAllStudents(ArrayList<Student> students) {
		for (Student st : students) {
			System.out.println(getDisplayInfo(st)); // call the method to print all student info
		}
	}

	// Writes the all given information to text file
	public static void saveStudents(ArrayList<Student> students, String filename) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			for (Student s : students) {
				StringBuilder sb = new StringBuilder();
				sb.append(s.name); // access the name

				for (int mark : s.attendanceMarks) {
					sb.append(",").append(mark); // takes the mark of attendance
				}

				bw.write(sb.toString()); // writes all the names and marks in the txt file
				bw.newLine(); // escapes a line
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Methods that reads the txt file
	public static ArrayList<Student> loadStudents(String filename) {
		ArrayList<Student> students = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Student s = new Student(); // create a new student
				s.name = parts[0]; // set the name as the first element

				for (int i = 1; i < parts.length; i++) {
					try {
						s.attendanceMarks.add(Integer.parseInt(parts[i]));
					} catch (NumberFormatException e) {
						System.out.println("Attendance mark is invalid: " + parts[i]);
						s.attendanceMarks.add(0);
					}
				}
				students.add(s); // add the student in the list
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + filename + " is not found");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return students;
	}

}