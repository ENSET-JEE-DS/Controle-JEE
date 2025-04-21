package application.controleapplication.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate dateReservation;
    private ReservationType reservationType;
    private int reservationDurationDays;
    private double reservationCost;
    private ReservationEtat reservationEtat;
    @ManyToOne
    private Entreprise entreprise;
}

