package JavaAndMongoDB;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class MongoDBSetup {
    /**
     * Main method to set up MongoDB environment and initialize collections.
     *
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("SchoolDB");
            MongoCollection<Document> studentsCollection = createStudentsCollection(database);
            MongoCollection<Document> coursesCollection = createCoursesCollection(database);
            insertSampleCourses(coursesCollection);
            insertSampleStudents(studentsCollection);
            createIndexOnStudentId(studentsCollection);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Creates the Students collection in the specified database.
     *
     * @param database The MongoDB database instance.
     * @return The created Students collection.
     */
    public static MongoCollection<Document> createStudentsCollection(MongoDatabase database) {
        MongoCollection<Document> studentsCollection = database.getCollection("Students");
        System.out.println("Students collection created.");
        return studentsCollection;
    }

    /**
     * Creates the Courses collection in the specified database.
     *
     * @param database The MongoDB database instance.
     * @return The created Courses collection.
     */
    public static MongoCollection<Document> createCoursesCollection(MongoDatabase database) {
        MongoCollection<Document> coursesCollection = database.getCollection("Courses");
        System.out.println("Courses collection created.");
        return coursesCollection;
    }

    /**
     * Inserts sample courses into the specified collection.
     *
     * @param coursesCollection The MongoDB collection to insert sample courses into.
     */
    public static void insertSampleCourses(MongoCollection<Document> coursesCollection) {
        List<Document> courses = Arrays.asList(
                new Document("courseId", "C001").append("courseName", "Mathematics").append("department", "Math"),
                new Document("courseId", "C002").append("courseName", "Physics").append("department", "Physics"),
                new Document("courseId", "C003").append("courseName", "Biology").append("department", "Biology")
        );
        coursesCollection.insertMany(courses);
        System.out.println("Sample courses inserted.");
    }

    /**
     * Inserts sample students into the specified collection.
     *
     * @param studentsCollection The MongoDB collection to insert sample students into.
     */
    public static void insertSampleStudents(MongoCollection<Document> studentsCollection) {
        List<Document> students = Arrays.asList(
                new Document("name", "Alice").append("age", 20).append("studentId", 1001).append("enrolledCourses", Arrays.asList(101, 102)),
                new Document("name", "Bob").append("age", 22).append("studentId", 1002).append("enrolledCourses", Arrays.asList(102, 103))
        );
        studentsCollection.insertMany(students);
        System.out.println("Sample students inserted.");
    }

    /**
     * Creates an index on the studentId field in the specified collection.
     *
     * @param studentsCollection The MongoDB collection to create an index on.
     */
    public static void createIndexOnStudentId(MongoCollection<Document> studentsCollection) {
        studentsCollection.createIndex(new Document("studentId", 1));
        System.out.println("Index created on studentId.");
    }
}
