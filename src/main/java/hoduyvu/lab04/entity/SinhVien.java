package hoduyvu.lab04.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"lop", "monHocs"})
@Entity(name = "SinhVien")
@Table(name = "SinhVien")
public class SinhVien {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MSSV", length = 10)
    @NotBlank(message = "MSSV không được để trống")
        @Pattern(regexp = "\\d{10}", message = "MSSV phải có đúng 10 chữ số")
    private String mssv;

    @Size(max = 50, message = "Họ tên chỉ tối đa 50 ký tự")
    @NotNull(message = "Họ và tên không được để trống")
    @Column(name = "HoTen", length = 50)
    private String hoTen;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "Ngày sinh không được lớn hơn ngày hiện tại")
    @Column(name = "NgaySinh")
    private Date ngaySinh;

    @Email(message = "Email phải hợp lệ")
    @Column(name = "Email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "MaLop",
            referencedColumnName = "MaLop",
            foreignKey = @ForeignKey(name = "FK_SINHVIEN_LOP")
    )
    private Lop lop;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SinhVien_MonHoc",
            joinColumns = {@JoinColumn(name = "MSSV")},
            inverseJoinColumns = {@JoinColumn(name = "MaMon")}
    )
    private Set<MonHoc> monHocs;
}
