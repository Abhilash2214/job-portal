package jobportal.service;

import java.util.ArrayList;
import java.util.List;
import jobportal.dto.RecruiterJobStats;
import jobportal.dto.DashboardSummaryDto;
import jobportal.repository.ApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import jobportal.exception.ResourceNotFoundException;
import jobportal.model.Job;
import jobportal.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job saveJob(Job job) {
        if (job.getStatus() == null) {
            job.setStatus("OPEN");
        }
        return jobRepository.save(job);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.setTitle(jobDetails.getTitle());
        job.setCompany(jobDetails.getCompany());
        job.setDescription(jobDetails.getDescription());
        job.setSalary(jobDetails.getSalary());

        return jobRepository.save(job);
    }

    public Job closeJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        job.setStatus("CLOSED");
        return jobRepository.save(job);
    }

    public String deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found");
        }
        jobRepository.deleteById(id);
        return "Job deleted successfully";
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jobRepository.findAll();
        }
        return jobRepository.searchByKeyword(keyword.trim());
    }
    @Autowired
    private ApplicationRepository applicationRepository;

    public DashboardSummaryDto getDashboardSummary() {
        List<Job> jobs = jobRepository.findAll();
        long totalJobs = jobs.size();
        long totalApplicants = applicationRepository.count();

        List<RecruiterJobStats> stats = new ArrayList<>();
        String mostAppliedTitle = "N/A";
        long mostAppliedCount = 0;

        for (Job job : jobs) {
            long count = applicationRepository.countByJobId(job.getId());
            stats.add(new RecruiterJobStats(job.getId(), job.getTitle(), job.getCompany(), count));
            if (count > mostAppliedCount) {
                mostAppliedCount = count;
                mostAppliedTitle = job.getTitle() + " (" + job.getCompany() + ")";
            }
        }

        return new DashboardSummaryDto(totalJobs, totalApplicants, mostAppliedTitle, mostAppliedCount, stats);
    }
}
