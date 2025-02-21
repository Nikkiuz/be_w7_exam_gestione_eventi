package it.epicode.be_w7_exam_gestione_eventi.auth;

import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUser;
import it.epicode.be_w7_exam_gestione_eventi.app_users.AppUserService;
import it.epicode.be_w7_exam_gestione_eventi.app_users.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<AppUser> adminUser = appUserService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }

        // Creazione dell'utente organizzatore se non esiste
        Optional<AppUser> orgUser = appUserService.findByUsername("organizzatore");
        if (orgUser.isEmpty()) {
            appUserService.registerUser("organizzatore", "orgpwd", Set.of(Role.ROLE_ORGANIZER));
        }

        // Creazione dell'utente user se non esiste
        Optional<AppUser> normalUser = appUserService.findByUsername("utente");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("utente", "utentepwd", Set.of(Role.ROLE_USER));
        }



    }
}
