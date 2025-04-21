package application.controleapplication.Controller;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Model.Reservation;
import application.controleapplication.Repository.EntrepriseRepository;
import application.controleapplication.Repository.ReservationRepository;
import jakarta.persistence.EntityManager;
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
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private ReservationRepository reservationRepository;
    private EntrepriseRepository entrepriseRepository;

    @GetMapping
    public String getAllReservations(
            Model model,
            @RequestParam(defaultValue = "", name = "kw") String keyword,
            @RequestParam(defaultValue = "0", name = "p") int page,
            @RequestParam(defaultValue = "4", name = "s") int pageSize
    ) {
        Page<Reservation> reservations = reservationRepository.findByEntrepriseNameEtrepriseContaining(keyword, PageRequest.of(page, pageSize));
        model.addAttribute("reservations", reservations);
        model.addAttribute("totalPages", reservations.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        return "reservations";
    }


    @GetMapping("/edit")
    public String editReservation(@RequestParam String id, Model model) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));
        model.addAttribute("reservation", reservation);
        return "edit-reservation";
    }

    @PostMapping("/edit")
    public String updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        return "redirect:/reservation";
    }

    @PostMapping("/delete")
    public String deleteReservation(@RequestParam String id) {
        reservationRepository.deleteById(id);
        return "redirect:/reservation";
    }

    @PostMapping("/add")
    public String addReservation(Reservation reservation) {
        System.out.println("Reservation to add: "+ reservation);
        reservationRepository.save(reservation);
        return "redirect:/reservation";
    }

    @GetMapping("/add")
    public String addReservation(Model model) { // Corrected method name
        model.addAttribute("reservation", new Reservation());
        return "add-reservation";
    }

}
