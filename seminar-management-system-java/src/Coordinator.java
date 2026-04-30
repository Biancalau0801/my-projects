/**
 * Coordinator (Faculty Staff) - System Requirement
 * Responsibilities:
 * - Creates and manages seminar sessions (date, venue, session type)
 * - Assigns evaluators and presenters to sessions
 * - Generates seminar schedules and final evaluation reports
 * - Oversees award nomination for Best Oral, Best Poster, and People's Choice
 */
public class Coordinator extends User {
    public Coordinator(String id, String name, String password) {
        super(id, name, password);
    }
    
    /**
     * Create a new seminar session
     */
    public Session createSession(String sessionId, String date, String time, String venue, 
                                 int capacity, String sessionType) {
        Session session = new Session(sessionId, date, time, venue, capacity, sessionType);
        Data.sessionList.add(session);
        return session;
    }
    
    /**
     * Assign an evaluator to a session
     */
    public boolean assignEvaluator(Session session, Evaluator evaluator) {
        if (session != null && evaluator != null) {
            session.setEvaluatorName(evaluator.getName() + " (" + evaluator.getId() + ")");
            return true;
        }
        return false;
    }
    
    /**
     * Assign a presenter (student) to a session
     */
    public boolean assignPresenter(Student student, Session session) {
        if (student == null || session == null) return false;
        
        int currentCount = Data.getStudentCountForSession(session.getSessionId());
        if (currentCount >= session.getCapacity()) {
            return false; // Session is full
        }
        
        student.setSessionId(session.getSessionId());
        return true;
    }
    
    /**
     * Nominate a student for Best Oral award
     */
    public void nominateBestOral(Student student) {
        if (student != null && student.getPresentationType().equals("Oral")) {
            student.setNominatedBestOral(true);
        }
    }
    
    /**
     * Nominate a student for Best Poster award
     */
    public void nominateBestPoster(Student student) {
        if (student != null && student.getPresentationType().equals("Poster")) {
            student.setNominatedBestPoster(true);
        }
    }
    
    /**
     * Generate seminar schedule report
     */
    public String generateSchedule() {
        return ReportGenerator.generateSeminarSchedule();
    }
    
    /**
     * Generate final evaluation report
     */
    public String generateEvaluationReport() {
        return ReportGenerator.generateEvaluationReport();
    }
    
    /**
     * Generate award report
     */
    public String generateAwardReport() {
        return ReportGenerator.generateAwardReport();
    }
    
    /**
     * Compute award winners based on scores and votes
     */
    public void computeAwardWinners() {
        ReportGenerator.computeAwardWinners();
    }
}