import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.NumberFormatException;

public class AttendanceApp{
				public static void main(String[] args){
								List<Student> studentList = new ArrayList<>();
				}
				
				// Helper methods
				public static void addStudent(ArrayList<Student> students, String name){
								Student st = new Student();
								students.add(st); //adds student to the arraylist
				}
				
				public static boolean recordAttendance(ArrayList<Student> students, String studentName, int mark){
								for(Student s: students){
												if(s.name.equalsIgnoreCase(studentName)){
																s.attendanceMarks.add(mark);
																return true; //if successfully found
												}
								}
								return false; //if not student is not found
				}
				
				public static double getAttendancePercentage(Student student){
								int total = student.attendanceMarks.size();
								int attended = 0;
								for(int mark: student.attendanceMarks){
												attended += mark; // add all present marks(1)
								}
								return ((attended / student.attendanceMarks.size()) * 100);
				}
				
				public static void getDisplayInfo(Student student){
								System.out.println("Name: " + student.name + ", Attendance: " + getAttendancePercentage(student));
				}
				
				public static void displayAllStudents(ArrayList<Student> students){
								for(Student st: students){
												getDisplayInfo(st);
								}
				}
				
				public static void saveStudents(ArrayList<Student> students, String filename){
								try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
												for (Student s: students){
																StringBuilder sb = new StringBuilder();
																sb.append(s.name); // access the name
																
																for (int mark : s.attendanceMarks){
																				sb.append(",").append(mark); // takes the mark of attendance
																}
																
																bw.write(sb.toString()); // writes all the names and marks in the txt file
																bw.newLine(); // escapes a line
												}
								} catch (IOException e){
												e.printStackTrace();
								}
				}
				
				public static List<Student> loadStudents(String filename){
								List<Student> students = new ArrayList<>();
								try (BufferedReader br = new BufferedReader (new FileReader(filename))){
												String line;
												while((line = br.readLine()) != null){
																String[] parts = line.split(",");
																Student s = new Student(); // create a new student
																s.name = parts[0]; // set the name as the first element
																
																for(int i = 0; i < parts.length; i++){
																				try {
																								s.attendanceMarks.add(Integer.parseInt(parts[i]))
																				} catch (NumberFormatException e){
																								System.out.println("Attendance mark is invalid: " + parts[i]);
																								s.attendanceMarks.add(0);
																				}
																}
																students.add(s); // add the student in the list
												}
								} catch (FileNotFoundException e){
												System.out.println("Error: " + filename + " is not found");
												
								} catch (IOException e){
												e.printStackTrace();
								}
								return students;
				}
}
