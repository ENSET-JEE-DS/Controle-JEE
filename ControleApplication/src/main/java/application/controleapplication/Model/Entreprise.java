package application.controleapplication.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entreprise {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nameEtreprise;
    private String username;
    private String email;
    private String activityDomain;
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    @Builder.Default
    List<Reservation> reservationList = new ArrayList<>();
}
