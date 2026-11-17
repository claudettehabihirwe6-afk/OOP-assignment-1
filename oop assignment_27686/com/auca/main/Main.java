package com.auca.main;

import com.auca.abstracts.*;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.*;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // In-memory record storage
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> feePayments = new ArrayList<>();
    private static final List<LibraryBook> libraryBooks = new ArrayList<>();
    private static final List<BookLoan> bookLoans = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                System.out.println("Exiting AUCA_ID System. Goodbye!");
                break;
            }
            try {
                int option = Integer.parseInt(input);
                switch (option) {
                    case 1 -> registerDepartment();
                    case 2 -> registerStudent();
                    case 3 -> registerTeacher();
                    case 4 -> registerAdministrator();
                    case 5 -> registerCourse();
                    case 6 -> registerClassroom();
                    case 7 -> enrollStudent();
                    case 8 -> recordAssessment();
                    case 9 -> recordFeePayment();
                    case 10 -> registerLibraryBook();
                    case 11 -> borrowLibraryBook();
                    case 12 -> returnLibraryBook();
                    case 13 -> viewAllRecords();
                    case 14 -> searchRecordById();
                    case 15 -> updateExistingRecord();
                    case 16 -> studentAcademicSummary();
                    case 17 -> studentPaymentHistory();
                    case 18 -> viewAvailableBooks();
                    case 19 -> demonstratePolymorphism();
                    default -> System.out.println("Invalid selection. Please choose an option from 0 to 19.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid menu number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMainMenu() {
        System.out.println("== AUCA_ID SCHOOL MANAGEMENT SYSTEM ==");
        System.out.println("1. Register Department");
        System.out.println("2. Register Student");
        System.out.println("3. Register Teacher");
        System.out.println("4. Register Administrator");
        System.out.println("5. Register Course");
        System.out.println("6. Register Classroom");
        System.out.println("7. Enroll Student");
        System.out.println("8. Record Assessment");
        System.out.println("9. Record Fee Payment");
        System.out.println("10. Register Library Book");
        System.out.println("11. Borrow Library Book");
        System.out.println("12. Return Library Book");
        System.out.println("13. View All Records");
        System.out.println("14. Search Record by ID");
        System.out.println("15. Update Existing Record");
        System.out.println("16. Student Academic Summary");
        System.out.println("17. Student Payment History");
        System.out.println("18. View Available Books");
        System.out.println("19. Demonstrate Polymorphism");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }

    // --- Helper Validation Routines ---
    private static String promptString(String fieldName) {
        while (true) {
            System.out.print(fieldName + ": ");
            String val = scanner.nextLine().trim();
            if (val.equalsIgnoreCase("CANCEL")) return null;
            if (!val.isEmpty()) return val;
            System.out.println("Value cannot be blank. Try again (or type CANCEL):");
        }
    }

    private static LocalDate promptDate(String fieldName) {
        while (true) {
            System.out.print(fieldName + " (YYYY-MM-DD): ");
            String val = scanner.nextLine().trim();
            if (val.equalsIgnoreCase("CANCEL")) return null;
            try {
                return LocalDate.parse(val);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private static int promptInt(String fieldName, int min, int max) {
        while (true) {
            System.out.print(fieldName + " (" + min + "-" + max + "): ");
            String val = scanner.nextLine().trim();
            if (val.equalsIgnoreCase("CANCEL")) return -999;
            try {
                int num = Integer.parseInt(val);
                if (num >= min && num <= max) return num;
                System.out.println("Value out of range [" + min + " - " + max + "].");
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer.");
            }
        }
    }

    private static double promptDouble(String fieldName, double min, double max) {
        while (true) {
            System.out.print(fieldName + " (" + min + "-" + max + "): ");
            String val = scanner.nextLine().trim();
            if (val.equalsIgnoreCase("CANCEL")) return -999.0;
            try {
                double num = Double.parseDouble(val);
                if (num >= min && num <= max) return num;
                System.out.println("Value out of range.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid double number.");
            }
        }
    }

    private static BigDecimal promptBigDecimal(String fieldName) {
        while (true) {
            System.out.print(fieldName + ": ");
            String val = scanner.nextLine().trim();
            if (val.equalsIgnoreCase("CANCEL")) return null;
            try {
                BigDecimal bd = new BigDecimal(val);
                if (bd.compareTo(BigDecimal.ZERO) > 0) return bd;
                System.out.println("Amount must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid monetary amount.");
            }
        }
    }

    private static boolean promptBoolean(String fieldName) {
        while (true) {
            System.out.print(fieldName + " (1 for YES / 2 for NO): ");
            String val = scanner.nextLine().trim();
            if ("1".equals(val) || val.equalsIgnoreCase("true") || val.equalsIgnoreCase("yes")) return true;
            if ("2".equals(val) || val.equalsIgnoreCase("false") || val.equalsIgnoreCase("no")) return false;
            System.out.println("Invalid selection. Choose 1 (YES) or 2 (NO).");
        }
    }

    // --- Workflows ---

    private static Department selectOrRegisterDepartment() throws Exception {
        while (true) {
            if (departments.isEmpty()) {
                System.out.println("No Department registered. Prerequisite missing!");
                System.out.println("1. Register Department now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    registerDepartment();
                    if (departments.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE DEPARTMENT ---");
                for (int i = 0; i < departments.size(); i++) {
                    System.out.println((i + 1) + ". " + departments.get(i).getDisplayName());
                }
                System.out.println((departments.size() + 1) + ". [+] Register a new Department");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= departments.size()) return departments.get(idx - 1);
                    if (idx == departments.size() + 1) {
                        registerDepartment();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static Teacher selectOrRegisterTeacher() throws Exception {
        while (true) {
            if (teachers.isEmpty()) {
                System.out.println("No Teacher registered. Prerequisite missing!");
                System.out.println("1. Register Teacher now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    registerTeacher();
                    if (teachers.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE TEACHER ---");
                for (int i = 0; i < teachers.size(); i++) {
                    System.out.println((i + 1) + ". " + teachers.get(i).getDisplayName());
                }
                System.out.println((teachers.size() + 1) + ". [+] Register a new Teacher");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= teachers.size()) return teachers.get(idx - 1);
                    if (idx == teachers.size() + 1) {
                        registerTeacher();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static Student selectOrRegisterStudent() throws Exception {
        while (true) {
            if (students.isEmpty()) {
                System.out.println("No Student registered. Prerequisite missing!");
                System.out.println("1. Register Student now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    registerStudent();
                    if (students.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE STUDENT ---");
                for (int i = 0; i < students.size(); i++) {
                    System.out.println((i + 1) + ". " + students.get(i).getDisplayName() + " (" + students.get(i).getStudentNumber() + ")");
                }
                System.out.println((students.size() + 1) + ". [+] Register a new Student");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= students.size()) return students.get(idx - 1);
                    if (idx == students.size() + 1) {
                        registerStudent();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static Course selectOrRegisterCourse() throws Exception {
        while (true) {
            if (courses.isEmpty()) {
                System.out.println("No Course registered. Prerequisite missing!");
                System.out.println("1. Register Course now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    registerCourse();
                    if (courses.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE COURSE ---");
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println((i + 1) + ". " + courses.get(i).getDisplayName());
                }
                System.out.println((courses.size() + 1) + ". [+] Register a new Course");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= courses.size()) return courses.get(idx - 1);
                    if (idx == courses.size() + 1) {
                        registerCourse();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static Enrollment selectOrRegisterEnrollment() throws Exception {
        while (true) {
            if (enrollments.isEmpty()) {
                System.out.println("No Enrollment registered. Prerequisite missing!");
                System.out.println("1. Enroll Student now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    enrollStudent();
                    if (enrollments.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE ENROLLMENT ---");
                for (int i = 0; i < enrollments.size(); i++) {
                    System.out.println((i + 1) + ". " + enrollments.get(i).getDisplayName() + " [" + enrollments.get(i).getSemester() + "]");
                }
                System.out.println((enrollments.size() + 1) + ". [+] Create new Enrollment");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= enrollments.size()) return enrollments.get(idx - 1);
                    if (idx == enrollments.size() + 1) {
                        enrollStudent();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static LibraryBook selectOrRegisterLibraryBook() throws Exception {
        while (true) {
            if (libraryBooks.isEmpty()) {
                System.out.println("No Library Book registered. Prerequisite missing!");
                System.out.println("1. Register Library Book now | 0. Cancel");
                System.out.print("Select: ");
                String ch = scanner.nextLine().trim();
                if ("1".equals(ch)) {
                    registerLibraryBook();
                    if (libraryBooks.isEmpty()) return null;
                } else {
                    return null;
                }
            } else {
                System.out.println("--- CHOOSE LIBRARY BOOK ---");
                for (int i = 0; i < libraryBooks.size(); i++) {
                    System.out.println((i + 1) + ". " + libraryBooks.get(i).getDisplayName() + " (Available: " + libraryBooks.get(i).getAvailableCopies() + ")");
                }
                System.out.println((libraryBooks.size() + 1) + ". [+] Register a new Library Book");
                System.out.println("0. Cancel");
                System.out.print("Select option: ");
                String ch = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(ch);
                    if (idx == 0) return null;
                    if (idx > 0 && idx <= libraryBooks.size()) return libraryBooks.get(idx - 1);
                    if (idx == libraryBooks.size() + 1) {
                        registerLibraryBook();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection.");
                }
            }
        }
    }

    // 1. Department
    private static void registerDepartment() {
        System.out.println("=== REGISTER DEPARTMENT ===");
        try {
            String depId = promptString("Department ID"); if (depId == null) return;
            for (Department d : departments) {
                if (d.getDepartmentId().equalsIgnoreCase(depId)) throw new DuplicateRecordException("Department ID already exists.");
            }
            String name = promptString("Department Name"); if (name == null) return;
            String code = promptString("Department Code"); if (code == null) return;
            String location = promptString("Office Location"); if (location == null) return;
            String email = promptString("Contact Email"); if (email == null) return;

            Teacher head = null;
            if (!teachers.isEmpty()) {
                System.out.print("Assign Head Teacher? (1-YES / 2-NO): ");
                if ("1".equals(scanner.nextLine().trim())) {
                    head = selectOrRegisterTeacher();
                }
            }

            Department dep = new Department(depId, name, code, location, email, head);
            dep.validate();

            System.out.println("Review: " + dep);
            System.out.print("1. Save record | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                departments.add(dep);
                System.out.println("Department registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 2. Student
    private static void registerStudent() {
        System.out.println("=== REGISTER STUDENT ===");
        try {
            String personId = promptString("01 Person ID"); if (personId == null) return;
            String firstName = promptString("02 First name"); if (firstName == null) return;
            String lastName = promptString("03 Last name"); if (lastName == null) return;
            String email = promptString("04 Personal Email"); if (email == null) return;
            String phone = promptString("05 Phone"); if (phone == null) return;
            LocalDate dob = promptDate("06 Date of Birth"); if (dob == null) return;

            String memberNo = promptString("07 Member Number"); if (memberNo == null) return;
            Department dep = selectOrRegisterDepartment(); if (dep == null) return;
            LocalDate joinDate = promptDate("09 Join Date"); if (joinDate == null) return;
            String status = promptString("10 Academic Status (ACTIVE/INACTIVE)"); if (status == null) return;
            String instEmail = promptString("11 Institutional Email"); if (instEmail == null) return;
            String advisor = promptString("12 Advisor Name"); if (advisor == null) return;

            String stuNo = promptString("13 Student Number"); if (stuNo == null) return;
            for (Student s : students) {
                if (s.getStudentNumber().equalsIgnoreCase(stuNo)) throw new DuplicateRecordException("Student number already exists.");
            }
            String prog = promptString("14 Program Name"); if (prog == null) return;
            LocalDate admDate = promptDate("15 Admission Date"); if (admDate == null) return;
            int studyYear = promptInt("16 Study Year", 1, 6); if (studyYear == -999) return;
            String gPhone = promptString("17 Guardian Phone"); if (gPhone == null) return;
            boolean scholarship = promptBoolean("18 Scholarship Status");

            Student student = new Student(personId, firstName, lastName, email, phone, dob, memberNo, dep, joinDate, status, instEmail, advisor, stuNo, prog, admDate, studyYear, gPhone, scholarship);
            student.validate();

            System.out.println("REVIEW STUDENT: " + student.getRegistrationSummary());
            System.out.print("1. Save student | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                students.add(student);
                System.out.println("Student " + stuNo + " registered successfully.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 3. Teacher
    private static void registerTeacher() {
        System.out.println("=== REGISTER TEACHER ===");
        try {
            String personId = promptString("Person ID"); if (personId == null) return;
            String firstName = promptString("First Name"); if (firstName == null) return;
            String lastName = promptString("Last Name"); if (lastName == null) return;
            String email = promptString("Email"); if (email == null) return;
            String phone = promptString("Phone"); if (phone == null) return;
            LocalDate dob = promptDate("Date of Birth"); if (dob == null) return;

            String memberNo = promptString("Member Number"); if (memberNo == null) return;
            Department dep = selectOrRegisterDepartment(); if (dep == null) return;
            LocalDate joinDate = promptDate("Join Date"); if (joinDate == null) return;
            String status = promptString("Academic Status"); if (status == null) return;
            String instEmail = promptString("Institutional Email"); if (instEmail == null) return;
            String advisor = promptString("Advisor/Mentor Name"); if (advisor == null) return;

            String empNo = promptString("Employee Number"); if (empNo == null) return;
            for (Teacher t : teachers) {
                if (t.getEmployeeNumber().equalsIgnoreCase(empNo)) throw new DuplicateRecordException("Employee number exists.");
            }
            String spec = promptString("Specialization"); if (spec == null) return;
            LocalDate hireDate = promptDate("Hire Date"); if (hireDate == null) return;
            BigDecimal salary = promptBigDecimal("Salary"); if (salary == null) return;
            String office = promptString("Office Number"); if (office == null) return;
            String empType = promptString("Employment Type"); if (empType == null) return;

            Teacher teacher = new Teacher(personId, firstName, lastName, email, phone, dob, memberNo, dep, joinDate, status, instEmail, advisor, empNo, spec, hireDate, salary, office, empType);
            teacher.validate();

            System.out.println("Review: " + teacher.getRegistrationSummary());
            System.out.print("1. Save Teacher | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                teachers.add(teacher);
                System.out.println("Teacher registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 4. Administrator
    private static void registerAdministrator() {
        System.out.println("=== REGISTER ADMINISTRATOR ===");
        try {
            String personId = promptString("Person ID"); if (personId == null) return;
            String firstName = promptString("First Name"); if (firstName == null) return;
            String lastName = promptString("Last Name"); if (lastName == null) return;
            String email = promptString("Email"); if (email == null) return;
            String phone = promptString("Phone"); if (phone == null) return;
            LocalDate dob = promptDate("Date of Birth"); if (dob == null) return;

            String staffNo = promptString("Staff Number"); if (staffNo == null) return;
            for (Administrator a : administrators) {
                if (a.getStaffNumber().equalsIgnoreCase(staffNo)) throw new DuplicateRecordException("Staff Number already exists.");
            }
            String title = promptString("Job Title"); if (title == null) return;
            String office = promptString("Assigned Office"); if (office == null) return;
            LocalDate empDate = promptDate("Employment Date"); if (empDate == null) return;
            int level = promptInt("Access Level", 1, 10); if (level == -999) return;
            boolean active = promptBoolean("Active Status");

            Administrator admin = new Administrator(personId, firstName, lastName, email, phone, dob, staffNo, title, office, empDate, level, active);
            admin.validate();

            System.out.println("Review: " + admin.getRegistrationSummary());
            System.out.print("1. Save Administrator | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                administrators.add(admin);
                System.out.println("Administrator registered successfully.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 5. Course
    private static void registerCourse() {
        System.out.println("=== REGISTER COURSE ===");
        try {
            String cId = promptString("Course ID"); if (cId == null) return;
            for (Course c : courses) {
                if (c.getCourseId().equalsIgnoreCase(cId)) throw new DuplicateRecordException("Course ID already exists.");
            }
            String code = promptString("Course Code"); if (code == null) return;
            String title = promptString("Course Title"); if (title == null) return;
            int credits = promptInt("Credit Hours", 1, 6); if (credits == -999) return;

            Department dep = selectOrRegisterDepartment(); if (dep == null) return;
            Teacher teacher = selectOrRegisterTeacher(); if (teacher == null) return;

            Course course = new Course(cId, code, title, credits, dep, teacher);
            course.validate();

            System.out.println("Review: " + course);
            System.out.print("1. Save Course | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                courses.add(course);
                System.out.println("Course registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 6. Classroom
    private static void registerClassroom() {
        System.out.println("=== REGISTER CLASSROOM ===");
        try {
            String cId = promptString("Classroom ID"); if (cId == null) return;
            for (Classroom c : classrooms) {
                if (c.getClassroomId().equalsIgnoreCase(cId)) throw new DuplicateRecordException("Classroom ID exists.");
            }
            String bName = promptString("Building Name"); if (bName == null) return;
            String rNo = promptString("Room Number"); if (rNo == null) return;
            int cap = promptInt("Capacity", 1, 500); if (cap == -999) return;
            int floor = promptInt("Floor Number", 0, 50); if (floor == -999) return;
            boolean avail = promptBoolean("Available");

            Classroom classroom = new Classroom(cId, bName, rNo, cap, floor, avail);
            classroom.validate();

            System.out.println("Review: " + classroom);
            System.out.print("1. Save Classroom | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                classrooms.add(classroom);
                System.out.println("Classroom registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // 7. Enroll Student
    private static void enrollStudent() {
        System.out.println("=== ENROLL STUDENT ===");
        try {
            Student student = selectOrRegisterStudent(); if (student == null) return;
            Course course = selectOrRegisterCourse(); if (course == null) return;
            String semester = promptString("Semester (e.g., 2026-Sem1)"); if (semester == null) return;

            // Check duplicate active enrollment
            for (Enrollment en : enrollments) {
                if (en.getStudent().getId().equals(student.getId()) &&
                    en.getCourse().getId().equals(course.getId()) &&
                    en.getSemester().equalsIgnoreCase(semester) &&
                    en.isActive()) {
                    throw new DuplicateRecordException("Student is already actively enrolled in this course for semester " + semester);
                }
            }

            String eId = promptString("Enrollment ID"); if (eId == null) return;
            LocalDate date = promptDate("Enrollment Date"); if (date == null) return;
            String status = promptString("Enrollment Status (ACTIVE/COMPLETED/DROPPED)"); if (status == null) return;

            Enrollment enrollment = new Enrollment(eId, student, course, date, semester, status);
            enrollment.validate();

            System.out.println("Review: " + enrollment);
            System.out.print("1. Save Enrollment | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                enrollments.add(enrollment);
                System.out.println("Enrollment registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }

    // 8. Record Assessment
    private static void recordAssessment() {
        System.out.println("=== RECORD ASSESSMENT ===");
        try {
            Enrollment enrollment = selectOrRegisterEnrollment(); if (enrollment == null) return;
            if (!enrollment.isActive()) {
                throw new ValidationException("Can only record assessment for ACTIVE enrollments.");
            }

            String aId = promptString("Assessment ID"); if (aId == null) return;
            String type = promptString("Assessment Type (QUIZ/EXAM/ASSIGNMENT)"); if (type == null) return;
            double maxScore = promptDouble("Maximum Score", 1.0, 100.0); if (maxScore == -999.0) return;
            double obtScore = promptDouble("Obtained Score", 0.0, maxScore); if (obtScore == -999.0) return;
            LocalDate aDate = promptDate("Assessment Date"); if (aDate == null) return;

            Assessment assessment = new Assessment(aId, enrollment, type, maxScore, obtScore, aDate);
            assessment.validate();

            System.out.println("Review: " + assessment);
            System.out.print("1. Save Assessment | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                assessments.add(assessment);
                System.out.println("Assessment recorded successfully!");
            }
        } catch (Exception e) {
            System.out.println("Assessment failure: " + e.getMessage());
        }
    }

    // 9. Record Fee Payment
    private static void recordFeePayment() {
        System.out.println("=== RECORD FEE PAYMENT ===");
        try {
            Student student = selectOrRegisterStudent(); if (student == null) return;

            String receiptNo = promptString("Receipt Number"); if (receiptNo == null) return;
            for (FeePayment fp : feePayments) {
                if (fp.getReceiptNumber().equalsIgnoreCase(receiptNo)) throw new DuplicateRecordException("Receipt number already exists.");
            }

            String pId = promptString("Payment ID"); if (pId == null) return;
            BigDecimal amount = promptBigDecimal("Amount"); if (amount == null) return;
            LocalDate pDate = promptDate("Payment Date"); if (pDate == null) return;
            String method = promptString("Payment Method (CASH/CARD/BANK)"); if (method == null) return;

            FeePayment payment = new FeePayment(pId, student, amount, pDate, method, receiptNo);
            payment.validate();

            System.out.println("Review: " + payment);
            System.out.print("1. Save Fee Payment | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                feePayments.add(payment);
                System.out.println("Fee payment recorded successfully!");
            }
        } catch (Exception e) {
            System.out.println("Payment failed: " + e.getMessage());
        }
    }

    // 10. Register Library Book
    private static void registerLibraryBook() {
        System.out.println("=== REGISTER LIBRARY BOOK ===");
        try {
            String bId = promptString("Book ID"); if (bId == null) return;
            for (LibraryBook b : libraryBooks) {
                if (b.getBookId().equalsIgnoreCase(bId)) throw new DuplicateRecordException("Book ID already exists.");
            }
            String isbn = promptString("ISBN"); if (isbn == null) return;
            String title = promptString("Title"); if (title == null) return;
            String author = promptString("Author"); if (author == null) return;
            int total = promptInt("Total Copies", 1, 1000); if (total == -999) return;
            int available = promptInt("Available Copies", 0, total); if (available == -999) return;

            LibraryBook book = new LibraryBook(bId, isbn, title, author, total, available);
            book.validate();

            System.out.println("Review: " + book);
            System.out.print("1. Save Book | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                libraryBooks.add(book);
                System.out.println("Book registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Book registration failed: " + e.getMessage());
        }
    }

    // 11. Borrow Library Book
    private static void borrowLibraryBook() {
        System.out.println("=== BORROW LIBRARY BOOK ===");
        try {
            Student student = selectOrRegisterStudent(); if (student == null) return;
            LibraryBook book = selectOrRegisterLibraryBook(); if (book == null) return;

            if (book.getAvailableCopies() <= 0) {
                throw new BookUnavailableException("Book '" + book.getTitle() + "' has no copies available for loan.");
            }

            String lId = promptString("Loan ID"); if (lId == null) return;
            LocalDate bDate = promptDate("Borrowed Date"); if (bDate == null) return;
            LocalDate dDate = promptDate("Due Date"); if (dDate == null) return;

            BookLoan loan = new BookLoan(lId, student, book, bDate, dDate, null);
            loan.validate();

            System.out.println("Review: " + loan);
            System.out.print("1. Confirm Borrowing | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                bookLoans.add(loan);
                System.out.println("Book borrowed successfully!");
            }
        } catch (Exception e) {
            System.out.println("Borrowing failed: " + e.getMessage());
        }
    }

    // 12. Return Library Book
    private static void returnLibraryBook() {
        System.out.println("=== RETURN LIBRARY BOOK ===");
        try {
            List<BookLoan> activeLoans = new ArrayList<>();
            for (BookLoan bl : bookLoans) {
                if (bl.isActive()) activeLoans.add(bl);
            }

            if (activeLoans.isEmpty()) {
                System.out.println("No active book loans found to return.");
                return;
            }

            for (int i = 0; i < activeLoans.size(); i++) {
                System.out.println((i + 1) + ". " + activeLoans.get(i).getDisplayName() + " [Loan ID: " + activeLoans.get(i).getId() + "]");
            }
            System.out.print("Select active loan to return (0 to Cancel): ");
            int ch = Integer.parseInt(scanner.nextLine().trim());
            if (ch <= 0 || ch > activeLoans.size()) return;

            BookLoan loan = activeLoans.get(ch - 1);
            LocalDate rDate = promptDate("Return Date"); if (rDate == null) return;

            if (rDate.isBefore(loan.getBorrowedDate())) {
                throw new ValidationException("Returned date cannot be before borrowed date (" + loan.getBorrowedDate() + ").");
            }

            loan.setReturnedDate(rDate);
            loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
            System.out.println("Book '" + loan.getBook().getTitle() + "' returned successfully! Stock updated.");

        } catch (Exception e) {
            System.out.println("Return failed: " + e.getMessage());
        }
    }

    // 13. View All Records
    private static void viewAllRecords() {
        System.out.println("=== SYSTEM RECORDS SUMMARY ===");
        System.out.println("Departments (" + departments.size() + "):");
        departments.forEach(System.out::println);
        System.out.println("Students (" + students.size() + "):");
        students.forEach(System.out::println);
        System.out.println("Teachers (" + teachers.size() + "):");
        teachers.forEach(System.out::println);
        System.out.println("Administrators (" + administrators.size() + "):");
        administrators.forEach(System.out::println);
        System.out.println("Courses (" + courses.size() + "):");
        courses.forEach(System.out::println);
        System.out.println("Classrooms (" + classrooms.size() + "):");
        classrooms.forEach(System.out::println);
        System.out.println("Enrollments (" + enrollments.size() + "):");
        enrollments.forEach(System.out::println);
        System.out.println("Assessments (" + assessments.size() + "):");
        assessments.forEach(System.out::println);
        System.out.println("Fee Payments (" + feePayments.size() + "):");
        feePayments.forEach(System.out::println);
        System.out.println("Library Books (" + libraryBooks.size() + "):");
        libraryBooks.forEach(System.out::println);
        System.out.println("Book Loans (" + bookLoans.size() + "):");
        bookLoans.forEach(System.out::println);
    }

    // 14. Search Record by ID
    private static void searchRecordById() {
        System.out.print("Enter Record ID to Search: ");
        String searchId = scanner.nextLine().trim();
        boolean found = false;

        List<Identifiable> allEntities = new ArrayList<>();
        allEntities.addAll(departments);
        allEntities.addAll(students);
        allEntities.addAll(teachers);
        allEntities.addAll(administrators);
        allEntities.addAll(courses);
        allEntities.addAll(classrooms);
        allEntities.addAll(enrollments);
        allEntities.addAll(assessments);
        allEntities.addAll(feePayments);
        allEntities.addAll(libraryBooks);
        allEntities.addAll(bookLoans);

        for (Identifiable item : allEntities) {
            if (item.getId().equalsIgnoreCase(searchId)) {
                System.out.println("Record Found [" + item.getClass().getSimpleName() + "]: " + item.getDisplayName());
                System.out.println("Details: " + item.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No record found with ID: " + searchId);
        }
    }

    // 15. Update Existing Record
    private static void updateExistingRecord() {
        System.out.print("Enter Student Number to Update: ");
        String sNum = scanner.nextLine().trim();
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(sNum)) {
                System.out.println("Found Student: " + s.getDisplayName());
                System.out.print("Enter New Phone Number (or press Enter to keep '" + s.getPhoneNumber() + "'): ");
                String newPhone = scanner.nextLine().trim();
                if (!newPhone.isEmpty()) {
                    s.setPhoneNumber(newPhone);
                    System.out.println("Phone number updated!");
                }
                return;
            }
        }
        System.out.println("Student record not found.");
    }

    // 16. Student Academic Summary
    private static void studentAcademicSummary() {
        System.out.print("Enter Student Number: ");
        String sNum = scanner.nextLine().trim();
        Student found = null;
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(sNum)) {
                found = s; break;
            }
        }
        if (found == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("=== ACADEMIC SUMMARY FOR " + found.getDisplayName().toUpperCase() + " ===");
        System.out.println("Program: " + found.getProgramName() + " | Year: " + found.getStudyYear());
        System.out.println("Enrollments & Assessments:");
        for (Enrollment en : enrollments) {
            if (en.getStudent().getId().equals(found.getId())) {
                System.out.println("- Course: " + en.getCourse().getCourseTitle() + " (" + en.getSemester() + ")");
                for (Assessment ass : assessments) {
                    if (ass.getEnrollment().getId().equals(en.getId())) {
                        System.out.println("  * " + ass.getAssessmentType() + ": " + ass.getObtainedScore() + "/" + ass.getMaximumScore());
                    }
                }
            }
        }
    }

    // 17. Student Payment History
    private static void studentPaymentHistory() {
        System.out.print("Enter Student Number: ");
        String sNum = scanner.nextLine().trim();
        Student found = null;
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(sNum)) {
                found = s; break;
            }
        }
        if (found == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("=== PAYMENT HISTORY FOR " + found.getDisplayName().toUpperCase() + " ===");
        BigDecimal total = BigDecimal.ZERO;
        for (FeePayment fp : feePayments) {
            if (fp.getStudent().getId().equals(found.getId())) {
                System.out.println(fp);
                total = total.add(fp.getAmount());
            }
        }
        System.out.println("Total Paid: $" + total);
    }

    // 18. View Available Books
    private static void viewAvailableBooks() {
        System.out.println("=== AVAILABLE LIBRARY BOOKS ===");
        long count = 0;
        for (LibraryBook book : libraryBooks) {
            if (book.getAvailableCopies() > 0) {
                System.out.println(book);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No books currently available.");
        }
    }

    // 19. Demonstrate Polymorphism
    private static void demonstratePolymorphism() {
        System.out.println("=== DEMONSTRATING POLYMORPHISM ===");

        List<Person> peopleList = new ArrayList<>();
        peopleList.addAll(students);
        peopleList.addAll(teachers);
        peopleList.addAll(administrators);

        if (peopleList.isEmpty()) {
            System.out.println("No Person objects registered yet to demonstrate. Register Students/Teachers/Administrators first.");
            return;
        }

        System.out.println("1. Heterogeneous List<Person> - Dynamic Method Dispatch (getRoleDescription()):");
        for (Person p : peopleList) {
            System.out.println("Person [" + p.getId() + "] " + p.getDisplayName() + " -> Role: " + p.getRoleDescription());
        }

        System.out.println("2. Interface-based Polymorphism List<Identifiable>:");
        List<Identifiable> items = new ArrayList<>(peopleList);
        items.addAll(courses);
        items.addAll(libraryBooks);
        for (Identifiable item : items) {
            System.out.println("Identifiable Entity -> ID: " + item.getId() + " | Display: " + item.getDisplayName() + " | Active: " + item.isActive());
        }
    }
}
