package jobportal.dto;

import java.time.LocalDateTime;

public class ApplicantDto {

    private Long applicantId;
    private Long applicationId;
    private String name;
    private String email;
    private Long jobId;
    private LocalDateTime appliedAt;
    private String resumeFileName;
    private String status;

    public ApplicantDto() {}

    public ApplicantDto(Long applicantId, Long applicationId, String name, String email, Long jobId, LocalDateTime appliedAt, String resumeFileName, String status) {
        this.applicantId = applicantId;
        this.applicationId = applicationId;
        this.name = name;
        this.email = email;
        this.jobId = jobId;
        this.appliedAt = appliedAt;
        this.resumeFileName = resumeFileName;
        this.status = status;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
