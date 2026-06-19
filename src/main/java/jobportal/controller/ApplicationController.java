package jobportal.controller;

import jobportal.dto.ApplicantDto;
import jobportal.exception.BadRequestException;
import jobportal.model.Application;
import jobportal.model.User;
import jobportal.repository.ApplicationRepository;
import jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * POST /applications (multipart/form-data)
     * Body: jobId (form field) + resume (optional file)
     * USER role required — enforced by SecurityConfig.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Application applyForJob(
            Authentication authentication,
            @RequestParam("jobId") Long jobId,
            @RequestParam(value = "resume", required = false) MultipartFile resume) {

        if (jobId == null) {
            throw new BadRequestException("jobId is required");
        }
        // Obtain the currently logged-in user from the JWT principal
        User user = (User) authentication.getPrincipal();
        return applicationService.applyForJob(user.getId(), jobId, resume);
    }

    /** GET /applications/user/{userId} — authenticated */
    @GetMapping("/user/{userId}")
    public List<Long> getAppliedJobIds(@PathVariable Long userId) {
        return applicationService.getAppliedJobIds(userId);
    }

    /** GET /applications/check/{jobId} — authenticated */
    @GetMapping("/check/{jobId}")
    public Map<String, Boolean> hasApplied(
            Authentication authentication,
            @PathVariable Long jobId) {
        User user = (User) authentication.getPrincipal();
        return Map.of("applied", applicationService.hasApplied(user.getId(), jobId));
    }

    /** GET /applications/job/{jobId} — RECRUITER only (SecurityConfig) */
    @GetMapping("/job/{jobId}")
    public List<ApplicantDto> getApplicantsForJob(@PathVariable Long jobId) {
        return applicationService.getApplicantsForJob(jobId);
    }

    /** GET /applications/{id}/resume — RECRUITER only (SecurityConfig) */
    @GetMapping("/{id}/resume")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        Resource resource = applicationService.getResumeFile(id);
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new jobportal.exception.ResourceNotFoundException("Application not found"));
        String fileName = application.getResumeFileName() != null
                ? application.getResumeFileName()
                : "resume.pdf";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /** PUT /applications/{id}/status — RECRUITER only (SecurityConfig) */
    @PutMapping("/{id}/status")
    public Application updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) {
            throw new BadRequestException("status is required");
        }
        return applicationService.updateApplicationStatus(id, status);
    }
}
