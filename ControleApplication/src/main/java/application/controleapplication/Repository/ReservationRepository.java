package application.controleapplication.Repository;


import application.controleapplication.Model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    Page<Reservation> findByEntrepriseNameEtrepriseContaining(String entrepriseName, Pageable pageable);
}
