// package management.subscription.config;

// import org.springframework.data.domain.AuditorAware;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;

// import java.util.Optional;

// public class AuditorAwareImpl implements AuditorAware<String> {

//     @Override
//     public Optional<String> getCurrentAuditor() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//         if (authentication == null || !authentication.isAuthenticated()) {
//             return Optional.empty();
//         }

//         // Explicitly cast to org.springframework.security.core.userdetails.UserDetails
//         UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//         // Assuming the user's name is stored in the username
//         String username = userDetails.getUsername();
//         return Optional.of(username);
//     }
// }
