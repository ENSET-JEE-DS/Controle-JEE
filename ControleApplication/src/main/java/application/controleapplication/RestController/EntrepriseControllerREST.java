package application.controleapplication.RestController;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Repository.EntrepriseRepository;
import application.controleapplication.Repository.ReservationRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/entreprise")
@AllArgsConstructor
public class EntrepriseControllerREST {

    private ReservationRepository reservationRepository;
    private EntrepriseRepository entrepriseRepository;


    @GetMapping
    @ResponseBody
    public List<Entreprise> getAllEntreprise(
            @RequestParam(defaultValue = "", name = "kw") String keyword,
            @RequestParam(defaultValue = "0", name = "p") int page,
            @RequestParam(defaultValue = "4", name = "s") int pageSize
    ) {
        Page<Entreprise> entreprises = entrepriseRepository.findEntrepriseByNameEtrepriseContaining(keyword, PageRequest.of(page, pageSize));
//        Must Use DTO
        return entreprises.getContent();
    }


}
