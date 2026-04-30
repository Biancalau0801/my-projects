

import java.sql.*;
import java.util.*;

public class CourseDAO {

    // -------------------------------
    // Search courses by keyword
    // Equivalent to Spring JPA's 
    // findByCourseCodeContainingIgnoreCaseOrCourseNameContainingIgnoreCase
    // -------------------------------
    public List<Course> searchCourses(String keyword) throws Exception {
        List<Course> list = new ArrayList<>();

        // SQL query with LIKE for case-insensitive partial matching
        String sql = "SELECT * FROM courses WHERE course_code LIKE ? OR course_name LIKE ?";

        // Use your DBConnection class to get a database connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Add wildcards for fuzzy search
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            // Execute query and map result set to Course objects
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getLong("id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseName(rs.getString("course_name"));
                c.setCredits(rs.getInt("credits"));
                c.setLecturer(rs.getString("lecturer"));
                list.add(c); // add course to list
            }
        }

        // Return the list of matched courses
        return list;
    }
}
