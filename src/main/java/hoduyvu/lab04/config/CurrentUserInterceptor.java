package hoduyvu.lab04.config;

import hoduyvu.lab04.entity.Account;
import hoduyvu.lab04.repository.IAccountRepository;
import hoduyvu.lab04.services.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CurrentUserInterceptor implements HandlerInterceptor {

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        if (modelAndView == null) return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Account account = accountRepository.findByMssv(userDetails.getUsername()).orElse(null);
            modelAndView.addObject("currentUserName", account != null ? account.getHoTen() : userDetails.getHoTen());
            modelAndView.addObject("currentUserMssv", userDetails.getUsername()); // MSSV (username)
        }
    }
}
