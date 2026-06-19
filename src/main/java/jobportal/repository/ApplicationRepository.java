package jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jobportal.dto.ApplicantDto;
import jobportal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    List<Application> findByUserId(Long userId);
    long countByJobId(Long jobId);

    @Query("SELECT new jobportal.dto.ApplicantDto(u.id, a.id, u.name, u.email, a.jobId, a.appliedAt, a.resumeFileName, a.status) " +
           "FROM Application a JOIN User u ON a.userId = u.id " +
           "WHERE a.jobId = :jobId")
    List<ApplicantDto> findApplicantsByJobId(@Param("jobId") Long jobId);
}
