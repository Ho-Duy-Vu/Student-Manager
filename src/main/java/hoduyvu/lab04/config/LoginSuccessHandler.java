package hoduyvu.lab04.config;

import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.services.CustomUserDetails;
import hoduyvu.lab04.services.SinhVienService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SinhVienService sinhVienService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Kiểm tra role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            // USER - kiểm tra hồ sơ có đầy đủ không
            String mssv = authentication.getName();
            SinhVien sv = sinhVienService.getSinhVienById(mssv);

            boolean incomplete = sv == null
                    || sv.getEmail() == null || sv.getEmail().isBlank()
                    || sv.getNgaySinh() == null
                    || sv.getLop() == null;

            if (incomplete) {
                response.sendRedirect(request.getContextPath()
                        + "/sinhvien/edit/" + mssv + "?incomplete=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/home");
            }
        }
    }
}
