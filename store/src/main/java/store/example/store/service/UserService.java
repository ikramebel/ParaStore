package store.example.store.service;


import store.example.store.dto.request.RegisterRequest;
import store.example.store.entity.User;
import store.example.store.exception.ResourceNotFoundException;
import store.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service pour la gestion des utilisateurs
 * 
 * Cette classe contient la logique métier pour les opérations
 * liées aux utilisateurs (création, modification, recherche).
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur
     * 
     * @param registerRequest Données d'inscription
     * @return Utilisateur créé
     * @throws IllegalArgumentException Si l'email existe déjà
     */
    public User createUser(RegisterRequest registerRequest) {
        // Vérification de l'unicité de l'email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Création de l'utilisateur
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .role(User.Role.USER)
                .build();

        return userRepository.save(user);
    }

    /**
     * Trouve un utilisateur par son email
     * 
     * @param email Email de l'utilisateur
     * @return Utilisateur trouvé
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + email));
    }

    /**
     * Trouve un utilisateur par son ID
     * 
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
    }

    /**
     * Vérifie si un utilisateur existe avec cet email
     * 
     * @param email Email à vérifier
     * @return true si l'utilisateur existe
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Met à jour les informations d'un utilisateur
     * 
     * @param id ID de l'utilisateur
     * @param name Nouveau nom
     * @param phone Nouveau téléphone
     * @param address Nouvelle adresse
     * @return Utilisateur mis à jour
     */
    public User updateUser(Long id, String name, String phone, String address) {
        User user = findById(id);
        
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim());
        }
        if (address != null) {
            user.setAddress(address.trim());
        }
        
        return userRepository.save(user);
    }

    /**
     * Change le mot de passe d'un utilisateur
     * 
     * @param id ID de l'utilisateur
     * @param currentPassword Mot de passe actuel
     * @param newPassword Nouveau mot de passe
     * @throws IllegalArgumentException Si le mot de passe actuel est incorrect
     */
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = findById(id);
        
        // Vérification du mot de passe actuel
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect");
        }
        
        // Mise à jour du mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Récupère tous les utilisateurs (pour l'administration)
     * 
     * @return Liste de tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Supprime un utilisateur
     * 
     * @param id ID de l'utilisateur à supprimer
     */
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    /**
     * Récupère tous les utilisateurs (pour l'administration)
     * 
     * @return Liste de tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Met à jour le rôle d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param role Nouveau rôle
     * @return Utilisateur mis à jour
     */
    public User updateUserRole(Long userId, String role) {
        User user = findById(userId);
        
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            user.setRole(userRole);
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle invalide: " + role);
        }
    }

    /**
     * Met à jour le statut d'activation d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param enabled Statut d'activation
     * @return Utilisateur mis à jour
     */
    public User updateUserStatus(Long userId, boolean enabled) {
        User user = findById(userId);
        // Note: Cette fonctionnalité nécessiterait d'ajouter un champ 'enabled' à l'entité User
        // Pour l'instant, on retourne l'utilisateur tel quel
        return user;
    }

    /**
     * Compte le nombre total d'utilisateurs
     * 
     * @return Nombre total d'utilisateurs
     */
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    /**
     * Compte le nombre d'utilisateurs par rôle
     * 
     * @return Map avec le nombre d'utilisateurs par rôle
     */
    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getUserCountByRole() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    user -> user.getRole().name(),
                    java.util.stream.Collectors.counting()
                ));
    }

    /**
     * Récupère les utilisateurs par rôle
     * 
     * @param role Rôle recherché
     * @return Liste des utilisateurs avec ce rôle
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
}