package com.auca.assessment;

import com.auca.enrollment.Enrollment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public class Assessment implements Identifiable, Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType, double maximumScore, double obtainedScore, LocalDate assessmentDate) {
        this.assessmentId = assessmentId;
        this.enrollment = enrollment;
        this.assessmentType = assessmentType;
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    public double getMaximumScore() { return maximumScore; }
    public void setMaximumScore(double maximumScore) { this.maximumScore = maximumScore; }
    public double getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(double obtainedScore) { this.obtainedScore = obtainedScore; }
    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }

    @Override
    public String getId() { return assessmentId; }

    @Override
    public String getDisplayName() { return assessmentType + " - " + enrollment.getStudent().getDisplayName(); }

    @Override
    public boolean isActive() { return true; }

    @Override
    public void validate() throws ValidationException {
        if (assessmentId == null || assessmentId.trim().length() < MIN_ID_LENGTH || assessmentId.trim().length() > MAX_ID_LENGTH) {
            throw new ValidationException("Assessment ID length invalid.");
        }
        if (enrollment == null) throw new ValidationException("Enrollment is required.");
        if (assessmentType == null || assessmentType.trim().isEmpty()) throw new ValidationException("Assessment type cannot be blank.");
        if (maximumScore <= 0) throw new ValidationException("Maximum score must be strictly positive.");
        if (obtainedScore < 0 || obtainedScore > maximumScore) throw new ValidationException("Obtained score must be between 0 and maximum score.");
        if (assessmentDate == null) throw new ValidationException("Assessment date is required.");
    }

    @Override
    public void printDetails() { System.out.println(getRegistrationSummary()); }

    @Override
    public String getRegistrationSummary() {
        return "Assessment ID: " + assessmentId + " | Type: " + assessmentType + " | Student: " + enrollment.getStudent().getDisplayName() + " | Score: " + obtainedScore + "/" + maximumScore;
    }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
