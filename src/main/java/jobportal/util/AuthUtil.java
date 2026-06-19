package jobportal.util;

import jobportal.exception.UnauthorizedException;

public final class AuthUtil {

    public static final String ROLE_RECRUITER = "RECRUITER";
    public static final String ROLE_USER = "USER";

    private AuthUtil() {}

    public static void requireRecruiter(String role) {
        if (role == null || !ROLE_RECRUITER.equalsIgnoreCase(role)) {
            throw new UnauthorizedException("Only recruiters can perform this action");
        }
    }

    public static void requireUser(String role) {
        if (role == null || !ROLE_USER.equalsIgnoreCase(role)) {
            throw new UnauthorizedException("Only users can apply for jobs");
        }
    }
}
