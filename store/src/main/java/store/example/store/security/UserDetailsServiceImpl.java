package store.example.store.security;


import store.example.store.entity.User;
import store.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service UserDetailsService pour Spring Security
 * 
 * Cette classe charge les détails d'un utilisateur à partir de la base de données
 * pour l'authentification et l'autorisation.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur (email)
     * 
     * @param username Nom d'utilisateur (email)
     * @return UserDetails de l'utilisateur
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + username));

        return user; // L'entité User implémente UserDetails
    }

    /**
     * Charge un utilisateur par son ID
     * 
     * @param id ID de l'utilisateur
     * @return UserDetails de l'utilisateur
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé
     */
    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'ID : " + id));

        return user;
    }
}