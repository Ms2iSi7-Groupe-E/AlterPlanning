package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.util.DataEnvelop;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api")
public class TestController {


    @GetMapping("/test")
    public ResponseEntity getUsers() {

        new Thread(() -> {
            System.out.println("Begin heavy operation in separate thread");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
            System.out.println("End of heavy operation");
        }).start();

        return DataEnvelop.CreateEnvelop("test ok");
    }

}
