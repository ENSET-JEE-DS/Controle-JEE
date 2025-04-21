package application.controleapplication.Repository;

import application.controleapplication.Model.Entreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise, String> {
    Page<Entreprise> findEntrepriseByNameEtrepriseContaining(String nom, Pageable pageable);
}
