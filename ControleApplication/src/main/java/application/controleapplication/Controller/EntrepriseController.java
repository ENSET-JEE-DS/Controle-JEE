package application.controleapplication.Controller;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Repository.EntrepriseRepository;
import application.controleapplication.Repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/entreprise")
@AllArgsConstructor
public class EntrepriseController {

    private ReservationRepository reservationRepository;
    private EntrepriseRepository entrepriseRepository;


    @GetMapping
    public String getAllEntreprise(
            Model model,
            @RequestParam(defaultValue = "", name = "kw") String keyword,
            @RequestParam(defaultValue = "0", name = "p") int page,
            @RequestParam(defaultValue = "4", name = "s") int pageSize
    ) {
        Page<Entreprise> entreprises = entrepriseRepository.findEntrepriseByNameEtrepriseContaining(keyword, PageRequest.of(page, pageSize));
        model.addAttribute("entreprises", entreprises.getContent());
        model.addAttribute("totalPages", entreprises.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        return "entreprises";
    }
}
