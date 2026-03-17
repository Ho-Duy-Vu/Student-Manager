package hoduyvu.lab04.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "MSSV không được để trống")
    @Pattern(regexp = "\\d{10}", message = "MSSV phải có đúng 10 chữ số")
    @Column(name = "mssv", unique = true, nullable = false, length = 10)
    private String mssv;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 50, message = "Họ tên tối đa 50 ký tự")
    @Column(name = "hoTen", nullable = false, length = 50)
    private String hoTen;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;

    @Column(name = "role", nullable = false, length = 20)
    private String role = "ROLE_USER";
}
