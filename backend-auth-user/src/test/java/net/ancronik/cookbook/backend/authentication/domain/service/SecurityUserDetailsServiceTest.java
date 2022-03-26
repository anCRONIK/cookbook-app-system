package net.ancronik.cookbook.backend.authentication.domain.service;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.AdminRepository;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import net.ancronik.cookbook.backend.authentication.domain.mapper.BasicUserToUserDetailsMapper;
import net.ancronik.cookbook.backend.authentication.domain.mapper.Mapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag(TestTypes.UNIT)
public class SecurityUserDetailsServiceTest {

    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);

    private final AdminRepository mockAdminRepository = Mockito.mock(AdminRepository.class);

    private final Mapper<BasicUser, UserDetails> basicUserToUserDetailsMapper = new BasicUserToUserDetailsMapper();

    private final SecurityUserDetailsService securityUserDetailsService = new SecurityUserDetailsService(mockUserRepository, mockAdminRepository, basicUserToUserDetailsMapper);

    @Test
    public void loadUserByUsername_NullAndEmptyUsernameGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> securityUserDetailsService.loadUserByUsername(null));
        assertThrows(UsernameNotFoundException.class, () -> securityUserDetailsService.loadUserByUsername("   \t "));

        verifyNoInteractions(mockAdminRepository, mockUserRepository);
    }

    @Test
    public void loadUserByUsername_UserRepositoryThrowsException_PropagateException() {
        String username = "User1";

        when(mockUserRepository.findByUsername(username)).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> securityUserDetailsService.loadUserByUsername(username));

        verify(mockUserRepository).findByUsername(username);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockAdminRepository);
    }

    @Test
    public void loadUserByUsername_UserRepositoryReturnsUser_ReturnUserDetails() {
        String username = "User1";
        User user = new User();
        user.setUsername(username);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertNotNull(securityUserDetailsService.loadUserByUsername(username));

        verify(mockUserRepository).findByUsername(username);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockAdminRepository);
    }

    @Test
    public void loadUserByUsername_UserNotInUserRepositoryAdminRepositoryThrowsException_PropagateException() {
        String username = "User1";

        when(mockAdminRepository.findByUsername(username)).thenThrow(new ConcurrencyFailureException("test"));
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DataAccessException.class, () -> securityUserDetailsService.loadUserByUsername(username));

        verify(mockUserRepository).findByUsername(username);
        verify(mockAdminRepository).findByUsername(username);
        verifyNoMoreInteractions(mockUserRepository, mockAdminRepository);
    }

    @Test
    public void loadUserByUsername_UserNotInUserRepositoryNorAdminRepository_ThrowException() {
        String username = "User1";

        when(mockAdminRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> securityUserDetailsService.loadUserByUsername(username));

        verify(mockUserRepository).findByUsername(username);
        verify(mockAdminRepository).findByUsername(username);
        verifyNoMoreInteractions(mockUserRepository, mockAdminRepository);
    }

    @Test
    public void loadUserByUsername_userFoundInAdminRepository_ReturnDetails() {
        String username = "User1";
        Admin admin = new Admin();
        admin.setUsername(username);

        when(mockAdminRepository.findByUsername(username)).thenReturn(Optional.of(admin));
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertNotNull(securityUserDetailsService.loadUserByUsername(username));

        verify(mockUserRepository).findByUsername(username);
        verify(mockAdminRepository).findByUsername(username);
        verifyNoMoreInteractions(mockUserRepository, mockAdminRepository);
    }

}
