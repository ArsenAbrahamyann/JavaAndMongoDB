package JavaAndMongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;


public class StudentDAO {
    private final MongoCollection<Document> studentsCollection;

    /**
     * Constructor for initializing the StudentDAO with the studentsCollection.
     *
     * @param studentsCollection The MongoCollection representing the Students collection.
     */
    public StudentDAO(MongoCollection<Document> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    /**
     * Adds a new student document to the Students collection.
     *
     * @param name            The name of the student.
     * @param age             The age of the student.
     * @param studentId       The unique identifier of the student.
     * @param enrolledCourses The list of course IDs the student is enrolled in.
     */
    public void addStudent(String name, int age, int studentId, List<Integer> enrolledCourses) {
        if (name == null || name.isEmpty() || age <= 0 || studentId <= 0 || enrolledCourses == null) {
            throw new IllegalArgumentException("Invalid input parameters for adding a student.");
        }

        Document studentDoc = new Document("name", name)
                .append("age", age)
                .append("studentId", studentId)
                .append("enrolledCourses", enrolledCourses);
        studentsCollection.insertOne(studentDoc);
    }

    /**
     * Retrieves student documents based on the specified course ID.
     *
     * @param courseId The ID of the course.
     * @return A list of student documents enrolled in the specified course.
     */
    public List<Document> findStudentsByCourse(int courseId) {
        Document query = new Document("enrolledCourses", courseId);
        MongoCursor<Document> cursor = studentsCollection.find(query).iterator();
        List<Document> students = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                students.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return students;
    }

    /**
     * Updates the enrolled courses for a student.
     *
     * @param studentId         The ID of the student to update.
     * @param newEnrolledCourses The new list of course IDs the student is enrolled in.
     */
    public void updateStudent(int studentId, List<Integer> newEnrolledCourses) {
        if (studentId <= 0 || newEnrolledCourses == null) {
            throw new IllegalArgumentException("Invalid input parameters for updating a student.");
        }

        Document query = new Document("studentId", studentId);
        Document update = new Document("$set", new Document("enrolledCourses", newEnrolledCourses));
        studentsCollection.updateOne(query, update);
    }

    /**
     * Removes a student from the Students collection based on the specified student ID.
     *
     * @param studentId The ID of the student to delete.
     */
    public void deleteStudent(int studentId) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID for deleting a student.");
        }

        Document query = new Document("studentId", studentId);
        studentsCollection.deleteOne(query);
    }
}
