package json_ex.cardealer;



import json_ex.cardealer.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ConsoleRunner implements CommandLineRunner {
    private final SeedService seedService;
    @Autowired
    public ConsoleRunner(SeedService seedService) {
        this.seedService = seedService;
    }

    @Override
    public void run(String... args) throws Exception {
    //this.seedService.seedParts();

        this.seedService.seedCustomers();
    }
}
