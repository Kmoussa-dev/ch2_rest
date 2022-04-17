package com.example.ch2_rest.controleur;

import com.example.ch2_rest.modele.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
public class UtilisateurControleur {


    private static Map<String, Utilisateur> utilisateurs=new TreeMap<>();

    public static Map<String,Utilisateur> getUtilisateurs(){
        return utilisateurs;
    }
    static {
        Utilisateur moussa=new Utilisateur("moussa","moussa",false);
        Utilisateur admin=new Utilisateur("admin","admin",true);
        utilisateurs.put(moussa.getLogin(), moussa);
        utilisateurs.put(admin.getLogin(), admin);
    }

    @PostMapping("/utilisateur")
    public ResponseEntity<Utilisateur> enregistrerUtilisateur(@RequestBody Utilisateur utilisateur){
        Predicate<String> isOk= s ->(s!=null)&&(s.length()>=2);
        if(!isOk.test(utilisateur.getLogin())||!isOk.test(utilisateur.getPassword())){
            return ResponseEntity.badRequest().build();
        }
        if (utilisateurs.containsKey(utilisateur.getLogin())){
            return ResponseEntity.badRequest().build();
        }
        utilisateurs.put(utilisateur.getLogin(), utilisateur);

        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(utilisateur.getLogin()).toUri();

        return ResponseEntity.created(location).body(utilisateur);
    }

    @GetMapping("/utilisateur/{login}")
    public ResponseEntity<Utilisateur> findUtilisateurById(Principal principal, @PathVariable("login") String login){
        if(!principal.getName().equals(login)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(utilisateurs.containsKey(login)){
            return ResponseEntity.ok().body(utilisateurs.get(login));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/utilisateur2/{login}")
    @PreAuthorize("#login==authentication.principal.username")
    public ResponseEntity<Utilisateur> findUtilisateurById2(Principal principal, @PathVariable("login") String login){
        if(utilisateurs.containsKey(login)){
            return ResponseEntity.ok().body(utilisateurs.get(login));
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
