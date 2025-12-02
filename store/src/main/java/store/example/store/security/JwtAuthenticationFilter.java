package store.example.store.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre JWT pour l'authentification
 * 
 * Ce filtre intercepte chaque requête HTTP pour vérifier la présence
 * et la validité d'un token JWT dans l'en-tête Authorization.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Filtre principal qui traite chaque requête
     * 
     * @param request Requête HTTP
     * @param response Réponse HTTP
     * @param filterChain Chaîne de filtres
     * @throws ServletException En cas d'erreur de servlet
     * @throws IOException En cas d'erreur d'E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Extraction du token JWT de la requête
            String jwt = getJwtFromRequest(request);
            
            // Vérification et validation du token
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Extraction du nom d'utilisateur du token
                String username = tokenProvider.getUsernameFromJWT(jwt);
                
                // Chargement des détails de l'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Création de l'objet d'authentification
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Définition de l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Impossible de définir l'authentification utilisateur", ex);
        }
        
        // Continuation de la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT de l'en-tête Authorization de la requête
     * 
     * @param request Requête HTTP
     * @return Token JWT ou null s'il n'est pas présent
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        // Vérification du format "Bearer <token>"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Suppression de "Bearer "
        }
        
        return null;
    }
}
