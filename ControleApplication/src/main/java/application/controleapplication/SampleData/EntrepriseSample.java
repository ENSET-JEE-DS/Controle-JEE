package application.controleapplication.SampleData;

import application.controleapplication.Model.Entreprise;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EntrepriseSample {
    public static List<Entreprise> getSampleEntreprises() {
        return Arrays.asList(
                Entreprise.builder()
                        .id("1")
                        .nameEtreprise("TechCorp")
                        .username("techcorp_user")
                        .email("contact@techcorp.com")
                        .activityDomain("Technology")
                        .build(),
                Entreprise.builder()
                        .id("2")
                        .nameEtreprise("HealthPlus")
                        .username("healthplus_user")
                        .email("info@healthplus.com")
                        .activityDomain("Healthcare")
                        .build()
        );
    }
}