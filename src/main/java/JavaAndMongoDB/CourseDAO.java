package JavaAndMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private final MongoCollection<Document> coursesCollection;

    /**
     * Constructs a CourseDAO with the specified courses collection.
     *
     * @param coursesCollection The MongoDB collection representing Courses.
     */
    public CourseDAO(MongoCollection<Document> coursesCollection) {
        this.coursesCollection = coursesCollection;
    }

    /**
     * Adds a new course to the Courses collection.
     *
     * @param courseId   The unique identifier of the course.
     * @param courseName The name of the course.
     * @param department The department offering the course.
     */
    public void addCourse(String courseId, String courseName, String department) {
        Document courseDoc = new Document("courseId", courseId)
                .append("courseName", courseName)
                .append("department", department);
        coursesCollection.insertOne(courseDoc);
    }

    /**
     * Retrieves all courses from the Courses collection.
     *
     * @return A list of documents representing all courses.
     */
    public List<Document> getAllCourses() {
        MongoCursor<Document> cursor = coursesCollection.find().iterator();
        List<Document> courses = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                courses.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return courses;
    }

    /**
     * Updates the details of a course in the Courses collection.
     *
     * @param courseId   The unique identifier of the course to update.
     * @param courseName The new name of the course.
     * @param department The new department offering the course.
     */
    public void updateCourse(String courseId, String courseName, String department) {
        Document query = new Document("courseId", courseId);
        Document update = new Document("$set", new Document("courseName", courseName).append("department", department));
        coursesCollection.updateOne(query, update);
    }

    /**
     * Deletes a course from the Courses collection based on its courseId.
     *
     * @param courseId The unique identifier of the course to delete.
     */
    public void deleteCourse(String courseId) {
        Document query = new Document("courseId", courseId);
        coursesCollection.deleteOne(query);
    }

    /**
     * Retrieves a course from the Courses collection based on its courseId.
     *
     * @param courseId The unique identifier of the course to retrieve.
     * @return The document representing the course, or null if not found.
     */
    public Document getCourseById(String courseId) {
        Document query = new Document("courseId", courseId);
        return coursesCollection.find(query).first();
    }
}
