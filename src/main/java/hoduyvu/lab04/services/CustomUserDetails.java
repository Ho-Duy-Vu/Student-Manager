package hoduyvu.lab04.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String hoTen;

    public CustomUserDetails(String mssv, String matKhau,
                             Collection<? extends GrantedAuthority> authorities,
                             String hoTen) {
        super(mssv, matKhau, authorities);
        this.hoTen = hoTen;
    }

    public String getHoTen() {
        return hoTen;
    }
}
