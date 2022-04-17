package com.example.ch2_rest.controleur;


import com.example.ch2_rest.modele.Message;
import com.example.ch2_rest.modele.Utilisateur;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
public class MessageControleur {

    private static List<Message> messages=new ArrayList<>();
    private final AtomicLong uid=new AtomicLong(1L);


    @PostMapping("/message")
    public ResponseEntity<Message> publicationMessage(Principal principal,@RequestBody Message message){
        if (message==null){
            return ResponseEntity.badRequest().build();
        }
        String login= principal.getName();

        Message messageRec=new Message(uid.getAndIncrement(),login+":"+ message.getTexte());
        messages.add(messageRec);

        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(messageRec.getId()).toUri();

        return ResponseEntity.created(location).body(messageRec);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> findAll(){return ResponseEntity.ok().body(messages);}

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> findById(@PathVariable("id") Long id){
        Optional<Message> message=messages.stream().filter(message1 -> message1.getId()==id).findAny();
        if(message==null){
            return ResponseEntity.notFound().build();
        }
        Message messageRec=message.get();

        return ResponseEntity.ok().body(messageRec);
    }
    @PutMapping("/messages/{id}")
    public ResponseEntity<Message> majMessage(Principal principal,@PathVariable("id") Long id,@RequestBody Message message){
        for(Message imessage:messages){
            if(imessage.getId()==id){
                messages.remove(imessage);
            }
        }
        String login= principal.getName();
        Message messageRec=new Message(id,login+":"+message.getTexte());
        messages.add(messageRec);
        return ResponseEntity.ok().body(messageRec);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        Optional<Message> message=messages.stream().filter(message1 -> message1.getId()==id).findAny();
        if (message==null){
            return ResponseEntity.notFound().build();
        }
        Message messageRec=message.get();
        messages.remove(messageRec);
        return ResponseEntity.noContent().build();
    }

}
