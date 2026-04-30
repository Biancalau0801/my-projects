import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends User {
    private String researchTitle;
    private String supervisorName; 
    
    private String submissionStatus = "Pending";
    private String score = "-";      
    private String remark = "-";
    private String sessionId = "Unassigned"; 
    
    private String projectAbstract = "-"; 
    private String presentationType = "Oral"; 
    
    private int[] rubricScores = {0, 0, 0, 0}; 
    
    private List<Submission> submissionHistory = new ArrayList<>();
    
    // 【Award System】Award nomination fields
    private boolean nominatedBestOral = false;
    private boolean nominatedBestPoster = false;
    private int peoplesChoiceVotes = 0;
    private boolean wonAward = false;
    private String awardType = "None"; // "Best Oral", "Best Poster", "People's Choice"

    
    public Student(String id, String name, String password, String researchTitle, String supervisorName) {
        super(id, name, password);
        this.researchTitle = researchTitle;
        this.supervisorName = supervisorName;
    }

    // --- Getters & Setters ---
    public String getResearchTitle() { return researchTitle; }
    public void setResearchTitle(String researchTitle) { this.researchTitle = researchTitle; }
    
    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }

    public String getSubmissionStatus() { return submissionStatus; }
    public void setSubmissionStatus(String status) { this.submissionStatus = status; }
    
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int[] getRubricScores() { return rubricScores; }
    public void setRubricScores(int[] scores) { this.rubricScores = scores; }

    public String getProjectAbstract() { return projectAbstract; }
    public void setProjectAbstract(String projectAbstract) { this.projectAbstract = projectAbstract; }

    public String getPresentationType() { return presentationType; }
    public void setPresentationType(String presentationType) { this.presentationType = presentationType; }

    public void addSubmission(String fileName) {
        submissionHistory.add(new Submission(fileName));
        this.submissionStatus = "Submitted";
    }

    public List<Submission> getSortedSubmissions() {
        Collections.sort(submissionHistory);
        return submissionHistory;
    }
    
    public String getLastUploadTime() {
        if (submissionHistory.isEmpty()) return "Never";
        return getSortedSubmissions().get(0).getFormattedTime();
    }
    
    // 【Award System】Getters and Setters
    public boolean isNominatedBestOral() { return nominatedBestOral; }
    public void setNominatedBestOral(boolean nominatedBestOral) { this.nominatedBestOral = nominatedBestOral; }
    
    public boolean isNominatedBestPoster() { return nominatedBestPoster; }
    public void setNominatedBestPoster(boolean nominatedBestPoster) { this.nominatedBestPoster = nominatedBestPoster; }
    
    public int getPeoplesChoiceVotes() { return peoplesChoiceVotes; }
    public void setPeoplesChoiceVotes(int votes) { this.peoplesChoiceVotes = votes; }
    public void addPeoplesChoiceVote() { this.peoplesChoiceVotes++; }
    
    public boolean hasWonAward() { return wonAward; }
    public void setWonAward(boolean wonAward) { this.wonAward = wonAward; }
    
    public String getAwardType() { return awardType; }
    public void setAwardType(String awardType) { 
        this.awardType = awardType;
        this.wonAward = !awardType.equals("None");
    }
}