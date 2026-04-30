import java.util.ArrayList;

public class Data {
    public static ArrayList<Student> studentList = new ArrayList<>();
    public static ArrayList<Coordinator> coordinatorList = new ArrayList<>();
    public static ArrayList<Evaluator> evaluatorList = new ArrayList<>();
    public static ArrayList<Session> sessionList = new ArrayList<>();

    public static void init() {
        // 1. Users login id and password (id, name, password)
        coordinatorList.add(new Coordinator("C001", "Dr. Admin", "1"));
        evaluatorList.add(new Evaluator("E001", "Dr. Tan", "1"));

        // 2. Students login and password (id, name, password, research title, supervisor)
        Student s1 = new Student("S101", "Alice", "1", "AI Research", "Dr. Lee");
        s1.setPresentationType("Oral"); // Default Alice as Oral
        
        Student s2 = new Student("S102", "Bob", "1", "Data Science","Dr. Adam");
        s2.setPresentationType("Oral"); // 👈 Default Bob as Oral
        
        Student s3 = new Student("S103", "Charlie", "1", "Cyber Security", "Dr. Nick");
        s3.setPresentationType("Poster"); // 👈 Default Charlie as Poster
        
        // Pre-select：Alice and Bob in section SES-01 as default
        s1.setSessionId("SES-01");
        s2.setSessionId("SES-01");
        // Charlie will assign by Coordinator
        
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);

        // 3. Sessions
        // SES-01: - Oral Presentation (Section Id, Due Date, Due Time, Venue, Capacity, Section Type)
        Session ses1 = new Session("SES-01", "2026-02-10", "10:00 AM", "Lab 1", 5, "Oral");
        ses1.setEvaluatorName("Dr. Tan (E001)"); // Pre-assign to Dr.Tan
        
        // SES-02: Poster Presentation (Section Id, Due Date, Due Time, Venue, Capacity, Section Type)
        Session ses2 = new Session("SES-02", "2026-01-12", "02:00 PM", "Hall A", 5, "Poster");
        ses2.setEvaluatorName("Dr. Tan (E001)");

        sessionList.add(ses1);
        sessionList.add(ses2);
    }

    // Count section capacity
    public static int getStudentCountForSession(String sessionId) {
        int count = 0;
        for (Student s : studentList) {
            if (s.getSessionId() != null && s.getSessionId().equals(sessionId)) {
                count++;
            }
        }
        return count;
    }
}