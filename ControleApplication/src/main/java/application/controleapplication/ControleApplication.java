package application.controleapplication;

import application.controleapplication.Model.Entreprise;
import application.controleapplication.Model.Reservation;
import application.controleapplication.Repository.EntrepriseRepository;
import application.controleapplication.SampleData.EntrepriseSample;
import application.controleapplication.SampleData.ReservationSample;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ControleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleApplication.class, args);
    }

    @Bean
    CommandLineRunner run(EntrepriseRepository entrepriseRepository) {
        return args -> {
            System.out.println("Application Started Successfully with args: " + Arrays.toString(args));

            List<Entreprise> entrepriseList = EntrepriseSample.getSampleEntreprises();
            Entreprise entreprise1 = entrepriseList.get(0);
            Entreprise entreprise2 = entrepriseList.get(1);

            List<Reservation> reservationList = ReservationSample.getSampleReservations();
            Reservation reservation1 = reservationList.get(0);
            Reservation reservation2 = reservationList.get(1);

            entreprise1.getReservationList().add(reservation1);
            entreprise1.getReservationList().add(reservation2);

            entreprise2.getReservationList().add(reservation2);

            entrepriseRepository.saveAll(List.of(entreprise1, entreprise2));

//            System.out.println("Entreprise List: " + entrepriseList);
//            System.out.println("Reservation List: " + reservationList);
        };
    }
}
