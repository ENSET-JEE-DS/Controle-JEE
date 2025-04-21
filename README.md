# Contrôle-JEE

## 1. Schéma de l'architecture technique

![alt text](image-17.png)

_Excusez mon médiocre dessin_

## 2. Diagramme de classe

```mermaid
classDiagram

class Entreprise {
  id : String
  nameEntreprise : String
  username : String
  email : String
  activityDomain : String
  reservationList : List<Reservation>
}

class Reservation {
  id : String
  dateReservation : LocalDate
  reservationType : ReservationType
  reservationDurationDays : int
  reservationCost : double
  reservationEtat : ReservationEtat
  entreprise : Entreprise
}

class ReservationType {
  <<enumeration>>
  SALLE_CONFERENCE
  CENTRE_CALCUL
  SALLE_REUNION
}

class ReservationEtat {
  <<enumeration>>
  PENDING
  VALIDATED
  REJECTED
}

Entreprise "1" --> "*" Reservation
```

---

## 3. Couche DAO

### a. Entités JPA

#### **Classe `Reservation`**

```java
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

public enum ReservationType {
    SALLE_CONFERENCE, CENTRE_CALCUL, SALLE_REUNION
}

public enum ReservationEtat {
    PENDING, VANLIDATED, REJECTED
}
```

#### **Classe `Entreprise`**

```java
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
```

---

### b. Interface JPARepository

```java
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}

public interface EntrepriseRepository extends JpaRepository<Entreprise, String> {
}
```

---

### c. Test des couches DAO

#### **Création**

![alt text](image-2.png)

![alt text](image.png)

![alt text](image-1.png)

---

#### **Ajout**

```java
@Bean
CommandLineRunner run(EntrepriseRepository entrepriseRepository, ReservationRepository reservationRepository) {
    return args -> {
        System.out.println("Application Started Successfully with args: " + Arrays.toString(args));

        List<Entreprise> entrepriseList = EntrepriseSample.getSampleEntreprises();
        Entreprise entreprise1 = entrepriseList.get(0);
        Entreprise entreprise2 = entrepriseList.get(1);

        List<Reservation> reservationList = ReservationSample.getSampleReservations();
        Reservation reservation1 = reservationList.get(0);
        Reservation reservation2 = reservationList.get(1);
        Reservation reservation3 = reservationList.get(2);

        entreprise1.getReservationList().add(reservation1);
        entreprise1.getReservationList().add(reservation2);
        reservation1.setEntreprise(entreprise1);
        reservation2.setEntreprise(entreprise1);

        entreprise2.getReservationList().add(reservation3);
        reservation3.setEntreprise(entreprise2);

        entrepriseRepository.saveAll(List.of(entreprise1, entreprise2));

        entrepriseRepository.findAll().forEach(entreprise -> {
            System.out.println("Entreprise: ");
            System.out.println(entreprise.getId());
            System.out.println(entreprise.getNameEtreprise());
        });

        reservationRepository.findAll().forEach(reservation -> {
            System.out.println("Reservation: ");
            System.out.println(reservation.getId());
            System.out.println(reservation.getReservationDurationDays());
        });
    };
}
```

---

#### **Exemple de sortie**

```bash
Entreprise:
b5f70352-5bca-4701-9749-2f4b65fa36cc
TechCorp
Entreprise:
e83b4452-8bf4-4e1e-9ba9-9f56c45a3a38
HealthPlus
Hibernate: select r1_0.id,r1_0.date_reservation,r1_0.entreprise_id,r1_0.reservation_cost,r1_0.reservation_duration_days,r1_0.reservation_etat,r1_0.reservation_type from reservation r1_0
Hibernate: select e1_0.id,e1_0.activity_domain,e1_0.email,e1_0.name_etreprise,e1_0.username from entreprise e1_0 where e1_0.id=?
Hibernate: select e1_0.id,e1_0.activity_domain,e1_0.email,e1_0.name_etreprise,e1_0.username from entreprise e1_0 where e1_0.id=?
Reservation:
b607e277-7047-42a7-a566-03d08e57be0f
3
Reservation:
bb186ef4-2588-41a0-917c-69fbefc97422
5
Reservation:
dfd86393-3a2d-4900-b30a-b3539f6e9a4d
5
```

![alt text](image-4.png)
![alt text](image-3.png)

---

## 4. Couche Web

### Controllers

```java
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
----------------

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
```

### a. Thymeleaf

#### **Index Pages**

- **Reservation**
  ![alt text](image-5.png)

- **Entreprises**
  ![alt text](image-9.png)

---

#### **Pagination**

![alt text](image-8.png)
![alt text](image-7.png)
![alt text](image-6.png)

---

#### **Recherche**

- **ENTREPRISE**
  ![alt text](image-10.png)

- **RESERVATION**
  ![alt text](image-11.png)

- **DELETE**

![alt text](image-13.png)
![alt text](image-12.png)

- **UPDATE**

![alt text](image-16.png)
![alt text](image-15.png)
![alt text](image-14.png)

- **Ajout**

![alt text](image-18.png)
![alt text](image-19.png)

### b. REST API

il faudra utiliser DTO (data transfer object) pour éviter le problème d'appel récursive.
![alt text](image-20.png)
