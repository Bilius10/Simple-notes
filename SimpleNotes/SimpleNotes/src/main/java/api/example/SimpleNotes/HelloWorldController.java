package api.example.SimpleNotes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloWorld")
public class HelloWorldController {

    @PostMapping("/bomdia/{nome}")
    public ResponseEntity<String> bomdia(@PathVariable String nome) {

        if(nome.equalsIgnoreCase("pedro")){
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Bom dia "+nome);
    }

    @PostMapping("/boaTarde/{nome}")
    public ResponseEntity<String> boaTarde(@PathVariable String nome) {

        if(nome.equalsIgnoreCase("pedro")){
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Boa tarde "+nome);
    }
}
