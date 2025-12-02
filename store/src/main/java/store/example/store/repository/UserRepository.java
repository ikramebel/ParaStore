package store.example.store.repository;


import store.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité User
 * 
 * Cette interface étend JpaRepository pour fournir les opérations CRUD de base
 * et définit des méthodes de recherche personnalisées pour les utilisateurs.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son email
     * 
     * @param email L'email de l'utilisateur
     * @return Un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec cet email
     * 
     * @param email L'email à vérifier
     * @return true si un utilisateur existe avec cet email
     */
    boolean existsByEmail(String email);

    /**
     * Trouve un utilisateur par son nom
     * 
     * @param name Le nom de l'utilisateur
     * @return Un Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByName(String name);

    /**
     * Trouve tous les utilisateurs ayant un rôle spécifique
     * 
     * @param role Le rôle recherché
     * @return Liste des utilisateurs avec ce rôle
     */
    List<User> findByRole(User.Role role);
}