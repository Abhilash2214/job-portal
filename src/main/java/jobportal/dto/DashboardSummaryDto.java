package jobportal.dto;

import java.util.List;

public class DashboardSummaryDto {

    private long totalJobs;
    private long totalApplicants;
    private String mostAppliedJobTitle;
    private long mostAppliedJobCount;
    private List<RecruiterJobStats> jobStats;

    public DashboardSummaryDto() {
    }

    public DashboardSummaryDto(long totalJobs, long totalApplicants,
                               String mostAppliedJobTitle, long mostAppliedJobCount,
                               List<RecruiterJobStats> jobStats) {
        this.totalJobs = totalJobs;
        this.totalApplicants = totalApplicants;
        this.mostAppliedJobTitle = mostAppliedJobTitle;
        this.mostAppliedJobCount = mostAppliedJobCount;
        this.jobStats = jobStats;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public long getTotalApplicants() {
        return totalApplicants;
    }

    public void setTotalApplicants(long totalApplicants) {
        this.totalApplicants = totalApplicants;
    }

    public String getMostAppliedJobTitle() {
        return mostAppliedJobTitle;
    }

    public void setMostAppliedJobTitle(String mostAppliedJobTitle) {
        this.mostAppliedJobTitle = mostAppliedJobTitle;
    }

    public long getMostAppliedJobCount() {
        return mostAppliedJobCount;
    }

    public void setMostAppliedJobCount(long mostAppliedJobCount) {
        this.mostAppliedJobCount = mostAppliedJobCount;
    }

    public List<RecruiterJobStats> getJobStats() {
        return jobStats;
    }

    public void setJobStats(List<RecruiterJobStats> jobStats) {
        this.jobStats = jobStats;
    }
}
