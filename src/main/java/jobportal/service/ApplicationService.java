package jobportal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jobportal.dto.ApplicantDto;
import jobportal.exception.BadRequestException;
import jobportal.exception.ResourceNotFoundException;
import jobportal.model.Application;
import jobportal.model.Job;
import jobportal.repository.ApplicationRepository;
import jobportal.repository.JobRepository;
import jobportal.repository.UserRepository;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Value("${app.upload.dir:uploads/resumes}")
    private String uploadDir;

    public Application applyForJob(Long userId, Long jobId, MultipartFile resumeFile) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if ("CLOSED".equalsIgnoreCase(job.getStatus())) {
            throw new BadRequestException("This job is no longer accepting applications");
        }
        if (applicationRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new BadRequestException("You have already applied for this job");
        }

        String resumePath = null;
        String resumeFileName = null;

        if (resumeFile != null && !resumeFile.isEmpty()) {
            String originalName = resumeFile.getOriginalFilename();
            if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
                throw new BadRequestException("Only PDF files are allowed");
            }
            try {
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                String storedName = UUID.randomUUID() + "_" + originalName;
                Path filePath = uploadPath.resolve(storedName);
                Files.copy(resumeFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                resumePath = filePath.toString();
                resumeFileName = originalName;
            } catch (IOException e) {
                throw new BadRequestException("Failed to store resume file: " + e.getMessage());
            }
        }

        return applicationRepository.save(new Application(userId, jobId, resumePath, resumeFileName));
    }

    public List<Long> getAppliedJobIds(Long userId) {
        return applicationRepository.findByUserId(userId).stream()
                .map(Application::getJobId)
                .toList();
    }

    public boolean hasApplied(Long userId, Long jobId) {
        return applicationRepository.existsByUserIdAndJobId(userId, jobId);
    }

    public List<ApplicantDto> getApplicantsForJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found");
        }
        return applicationRepository.findApplicantsByJobId(jobId);
    }

    public Resource getResumeFile(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (application.getResumePath() == null) {
            throw new ResourceNotFoundException("No resume uploaded for this application");
        }
        try {
            Path filePath = Paths.get(application.getResumePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Resume file not found on server");
            }
            return resource;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not read resume file: " + e.getMessage());
        }
    }

    public Application updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("PENDING") && !upperStatus.equals("APPROVED") && !upperStatus.equals("REJECTED")) {
            throw new BadRequestException("Invalid status: " + status);
        }
        application.setStatus(upperStatus);
        return applicationRepository.save(application);
    }
}
