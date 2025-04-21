package application.controleapplication;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Model.Reservation;
import application.controleapplication.Repository.EntrepriseRepository;
import application.controleapplication.Repository.ReservationRepository;
import application.controleapplication.SampleData.EntrepriseSample;
import application.controleapplication.SampleData.ReservationSample;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ControleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleApplication.class, args);
    }

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

            entrepriseRepository.findAll().forEach(entreprise ->
            {
                System.out.println("Entreprise: ");
                System.out.println(entreprise.getId());
                System.out.println(entreprise.getNameEtreprise());
            });

            reservationRepository.findAll().forEach(reservation ->
            {
                System.out.println("Reservation: ");
                System.out.println(reservation.getId());
                System.out.println(reservation.getReservationDurationDays());
            });

//            Page<Entreprise> entreprises = entrepriseRepository.findEntrepriseByNameEtrepriseContaining("", PageRequest.of(0, 10));
//            entreprises.getContent().forEach(entreprise ->
//                    {
//                        System.out.println(entreprise.getNameEtreprise());
//                    }
//            );

        };
    }
}
