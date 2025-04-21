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
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        return "entreprises";
    }

    @GetMapping("/edit")
    public String editEntreprise(@RequestParam String id, Model model) {
        Entreprise entreprise = entrepriseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid entreprise ID: " + id));
        model.addAttribute("entreprise", entreprise);
        return "edit-entreprise";
    }

    @PostMapping("/edit")
    public String updateEntreprise(Entreprise entreprise) {
        entrepriseRepository.save(entreprise);
        return "redirect:/entreprise";
    }

    @PostMapping("/delete")
    public String deleteEntreprise(@RequestParam String id) {
        entrepriseRepository.deleteById(id);
        return "redirect:/entreprise";
    }

    @PostMapping("/add")
    public String addEntreprise(Entreprise entreprise) {
        System.out.println("Entreprise to add: "+entreprise);
        entrepriseRepository.save(entreprise);
        return "redirect:/entreprise";
    }

    @GetMapping("/add")
    public String addEntreprise(Model model) {
        model.addAttribute("entreprise", new Entreprise());
        return "add-entreprise";
    }

}
