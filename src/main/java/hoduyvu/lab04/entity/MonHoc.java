package hoduyvu.lab04.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"sinhViens"})
@Entity(name = "MonHoc")
@Table(name = "MonHoc")
public class MonHoc {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaMon", length = 10)
    @Size(min = 1, max = 10, message = "Mã môn phải từ 1 đến 10 ký tự")
    private String maMon;

    @Size(min = 5, max = 50, message = "Tên môn phải từ 5 đến 50 ký tự")
    @Column(name = "TenMonHoc", length = 50)
    private String tenMonHoc;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    @Column(name = "MoTa", length = 255)
    private String moTa;

    @NotNull(message = "Số lượng đăng ký không được để trống")
    @Min(value = 1, message = "Số lượng đăng ký phải lớn hơn 0")
    @Column(name = "SoLuongDangKy", nullable = false)
    private Integer soLuongDangKy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SinhVien_MonHoc",
            joinColumns = {@JoinColumn(name = "MaMon")},
            inverseJoinColumns = {@JoinColumn(name = "MSSV")}
    )
    private Set<SinhVien> sinhViens;

    @Transient
    public int getSoLuongDaDangKy() {
        return sinhViens == null ? 0 : sinhViens.size();
    }

    @Transient
    public boolean isConCho() {
        return soLuongDangKy != null && getSoLuongDaDangKy() < soLuongDangKy;
    }
}