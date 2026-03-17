package hoduyvu.lab04.services;

import hoduyvu.lab04.entity.Account;
import hoduyvu.lab04.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String mssv) throws UsernameNotFoundException {
        Account account = accountRepository.findByMssv(mssv)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với MSSV: " + mssv));

        return new CustomUserDetails(
                account.getMssv(),
                account.getMatKhau(),
                List.of(new SimpleGrantedAuthority(account.getRole())),
                account.getHoTen()
        );
    }
}
