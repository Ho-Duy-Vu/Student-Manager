package hoduyvu.lab04;

import hoduyvu.lab04.entity.Account;
import hoduyvu.lab04.entity.SinhVien;
import hoduyvu.lab04.repository.IAccountRepository;
import hoduyvu.lab04.services.SinhVienService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Lab04Application {

	private static final String DEFAULT_ADMIN_MSSV = "9999999999";
	private static final String DEFAULT_ADMIN_NAME = "Quản trị viên";
	private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

	public static void main(String[] args) {
		SpringApplication.run(Lab04Application.class, args);
	}

	@Bean
	CommandLineRunner seedDefaultAdmin(IAccountRepository accountRepository,
	                                  SinhVienService sinhVienService,
	                                  PasswordEncoder passwordEncoder) {
		return args -> {
			if (accountRepository.existsByMssv(DEFAULT_ADMIN_MSSV)) {
				return;
			}

			SinhVien sinhVien = sinhVienService.getSinhVienById(DEFAULT_ADMIN_MSSV);
			if (sinhVien == null) {
				sinhVien = new SinhVien();
				sinhVien.setMssv(DEFAULT_ADMIN_MSSV);
				sinhVien.setHoTen(DEFAULT_ADMIN_NAME);
				sinhVien.setEmail("");
				sinhVienService.addSinhVien(sinhVien);
			}

			Account adminAccount = new Account();
			adminAccount.setMssv(DEFAULT_ADMIN_MSSV);
			adminAccount.setHoTen(DEFAULT_ADMIN_NAME);
			adminAccount.setMatKhau(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
			adminAccount.setRole("ROLE_ADMIN");
			accountRepository.save(adminAccount);
		};
	}

}
