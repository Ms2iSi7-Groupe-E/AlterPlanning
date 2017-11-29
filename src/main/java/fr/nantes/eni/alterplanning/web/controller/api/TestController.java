package fr.nantes.eni.alterplanning.web.controller.api;

import fr.nantes.eni.alterplanning.web.util.DataEnvelop;
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
    public ResponseEntity testReponse() {
        return DataEnvelop.CreateEnvelop("toto");
    }


}
