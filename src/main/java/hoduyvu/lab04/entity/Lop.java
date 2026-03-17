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
@Entity(name = "Lop")
@Table(name = "Lop")
public class Lop {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLop")
    private Integer maLop;

    @Size(min = 1, max = 7, message = "Tên lớp phải từ 1 đến 7 ký tự")
    @NotNull(message = "Tên lớp không được để trống")
    @Column(name = "TenLop", length = 7)
    private String tenLop;

    @OneToMany(mappedBy = "lop", fetch = FetchType.LAZY)
    private Set<SinhVien> sinhViens;
}
