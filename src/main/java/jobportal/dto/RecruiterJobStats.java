package jobportal.dto;

public class RecruiterJobStats {

    private Long jobId;
    private String title;
    private String company;
    private Long applicationCount;

    public RecruiterJobStats() {
    }

    public RecruiterJobStats(Long jobId, String title,
                             String company, Long applicationCount) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.applicationCount = applicationCount;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Long getApplicationCount() {
        return applicationCount;
    }
}