package jobportal.controller;

import jobportal.dto.DashboardSummaryDto;
import jobportal.model.Job;
import jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    /** POST /jobs — RECRUITER only (enforced by SecurityConfig) */
    @PostMapping
    public Job addJob(@RequestBody Job job) {
        return jobService.saveJob(job);
    }

    /** GET /jobs/{id} — public */
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    /** PUT /jobs/{id} — RECRUITER only */
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        return jobService.updateJob(id, jobDetails);
    }

    /** PUT /jobs/{id}/close — RECRUITER only */
    @PutMapping("/{id}/close")
    public Job closeJob(@PathVariable Long id) {
        return jobService.closeJob(id);
    }

    /** DELETE /jobs/{id} — RECRUITER only */
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {
        return jobService.deleteJob(id);
    }

    /** GET /jobs — public */
    @GetMapping
    public List<Job> getAllJobs(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return jobService.searchJobs(search);
        }
        return jobService.getAllJobs();
    }

    /** GET /jobs/dashboard — RECRUITER only (enforced by SecurityConfig via authenticated()) */
    @GetMapping("/dashboard")
    public DashboardSummaryDto getDashboard() {
        return jobService.getDashboardSummary();
    }
}
