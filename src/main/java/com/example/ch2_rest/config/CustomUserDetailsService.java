package com.example.ch2_rest.config;

import com.example.ch2_rest.controleur.MessageControleur;
import com.example.ch2_rest.controleur.UtilisateurControleur;
import com.example.ch2_rest.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String[] ROLES_ADMIN={"USER","ADMIN"};
    private static final String[] ROLES_USER={"USER"};

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utilisateur utilisateur= UtilisateurControleur.getUtilisateurs().get(username);

        if(utilisateur==null){
            throw new UsernameNotFoundException("User "+username+" not found");
        }

        String[] roles =utilisateur.isAdmin() ? ROLES_ADMIN : ROLES_USER;

        UserDetails userDetails= User.builder()
                .username(utilisateur.getLogin())
                .password(passwordEncoder.encode(utilisateur.getPassword()))
                .roles(roles)
                .build();

        return userDetails;
    }

}
