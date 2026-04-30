public class Session {
    private String sessionId;
    private String date; // Presentation Date
    private String time;
    private String venue;
    private String evaluatorName = "Unassigned"; 
    private int capacity; 
    
    // [New] Submission Deadline (Defaults to presentation date, but can be changed)
    private String submissionDueDate;
    
    // [System Requirement] Session type (Oral/Poster)
    private String sessionType = "Oral";
    
    // =========================================================
    // [Critical Fix] Added Board ID field (Fixes CoordinatorMenu error)
    // =========================================================
    private String boardId = "N/A"; 
    

    public Session(String sessionId, String date, String time, String venue, int capacity) {
        this.sessionId = sessionId;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.capacity = capacity;
        this.submissionDueDate = date; // Default deadline = Presentation date
    }
    
    public Session(String sessionId, String date, String time, String venue, int capacity, String sessionType) {
        this(sessionId, date, time, venue, capacity);
        this.sessionType = sessionType;
    }

    // --- Getters ---
    public String getSessionId() { return sessionId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getVenue() { return venue; }
    public String getEvaluatorName() { return evaluatorName; }
    public int getCapacity() { return capacity; }
    public String getSubmissionDueDate() { return submissionDueDate; }
    public String getSessionType() { return sessionType; }
    
    // [Critical Fix] New Getter
    public String getBoardId() { return boardId; }

    // --- Setters ---
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setSubmissionDueDate(String submissionDueDate) { this.submissionDueDate = submissionDueDate; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    
    // [Critical Fix] New Setter
    public void setBoardId(String boardId) { this.boardId = boardId; }

    @Override
    public String toString() {
        return sessionId + " (" + date + ")";
    }
}