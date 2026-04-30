/**
 * Evaluator - System Requirement: Evaluation Module
 * Responsibilities:
 * - Score and comment using rubrics
 * - Evaluate student presentations based on criteria
 * - Provide detailed feedback
 */
public class Evaluator extends User {
    
    // Rubric criteria names
    public static final String[] RUBRIC_CRITERIA = {
    "Problem Clarity",
    "Methodology",
    "Results",
    "Presentation"
    };
    
    // Maximum score for each rubric item
    public static final int MAX_RUBRIC_SCORE = 25;
    
    public Evaluator(String id, String name, String password) {
        super(id, name, password);
    }
    
    /**
     * Evaluate a student with rubric scores
     * @param student The student to evaluate
     * @param rubricScores Array of 4 scores (0-25 each)
     * @param remark Evaluator's comments
     */
    public void evaluateStudent(Student student, int[] rubricScores, String remark) {
        if (student == null || rubricScores == null || rubricScores.length != 4) {
            return;
        }
        
        // Validate scores
        for (int i = 0; i < rubricScores.length; i++) {
            if (rubricScores[i] < 0) rubricScores[i] = 0;
            if (rubricScores[i] > MAX_RUBRIC_SCORE) rubricScores[i] = MAX_RUBRIC_SCORE;
        }
        
        // Set rubric scores
        student.setRubricScores(rubricScores);
        
        // Calculate total score
        int totalScore = 0;
        for (int score : rubricScores) {
            totalScore += score;
        }
        
        student.setScore(String.valueOf(totalScore));
        student.setRemark(remark != null ? remark : "No remarks");
        student.setSubmissionStatus("Evaluated");
    }
    
    /**
     * Quick evaluate with total score only
     */
    public void quickEvaluate(Student student, int totalScore, String remark) {
        if (student == null) return;
        
        student.setScore(String.valueOf(totalScore));
        student.setRemark(remark != null ? remark : "No remarks");
        student.setSubmissionStatus("Evaluated");
    }
    
    /**
     * Get rubric criteria description
     */
    public static String getRubricDescription(int index) {
        if (index < 0 || index >= RUBRIC_CRITERIA.length) {
            return "Unknown Criteria";
        }
        return RUBRIC_CRITERIA[index];
    }
    
    /**
     * Get detailed rubric guidelines
     */
    public static String getRubricGuidelines() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluation Rubric (Total: 100 points)\n");
        sb.append("=====================================\n\n");
        
        sb.append("1. Content & Organization (0-25 points):\n");
        sb.append("   - Clarity of research objectives\n");
        sb.append("   - Logical flow of presentation\n");
        sb.append("   - Depth of technical content\n\n");
        
        sb.append("2. Delivery & Presentation (0-25 points):\n");
        sb.append("   - Speaking clarity and confidence\n");
        sb.append("   - Time management\n");
        sb.append("   - Audience engagement\n\n");
        
        sb.append("3. Visual Aids & Materials (0-25 points):\n");
        sb.append("   - Quality of slides/poster\n");
        sb.append("   - Effective use of visuals\n");
        sb.append("   - Professional appearance\n\n");
        
        sb.append("4. Q&A & Engagement (0-25 points):\n");
        sb.append("   - Understanding of questions\n");
        sb.append("   - Quality of responses\n");
        sb.append("   - Critical thinking demonstrated\n");
        
        return sb.toString();
    }
}