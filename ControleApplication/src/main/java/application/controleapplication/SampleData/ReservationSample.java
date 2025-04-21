package application.controleapplication.SampleData;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Model.Reservation;
import application.controleapplication.Model.ReservationEtat;
import application.controleapplication.Model.ReservationType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class ReservationSample {
    public static List<Reservation> getSampleReservations() {
        return Arrays.asList(
                Reservation.builder()
                        .dateReservation(LocalDate.now())
                        .reservationType(ReservationType.SALLE_CONFERENCE)
                        .reservationDurationDays(3)
                        .reservationCost(500.0)
                        .reservationEtat(ReservationEtat.PENDING)
                        .build(),
                Reservation.builder()
                        .dateReservation(LocalDate.now().plusDays(1))
                        .reservationType(ReservationType.CENTRE_CALCUL)
                        .reservationDurationDays(5)
                        .reservationCost(1200.0)
                        .reservationEtat(ReservationEtat.VANLIDATED)
                        .build()
,
                Reservation.builder()
                        .dateReservation(LocalDate.now().plusDays(5))
                        .reservationType(ReservationType.SALLE_REUNION)
                        .reservationDurationDays(5)
                        .reservationCost(5000.0)
                        .reservationEtat(ReservationEtat.REJECTED)
                        .build()
        );
    }
}