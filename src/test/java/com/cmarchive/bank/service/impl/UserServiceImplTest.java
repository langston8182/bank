package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.domain.Role;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.UserNotFoundException;
import com.cmarchive.bank.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private final static Long ID = 1L;
    private final static String EMAIL = "email";
    private final static String PASSWORD = "password";
    private static final String ROLE_USER = "ROLE_USER";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void list_nominal() {
        List<User> users = mock(List.class);
        given(userRepository.findAllByOrderByNomAsc()).willReturn(users);

        List<User> resultat = userService.list();

        then(userRepository).should().findAllByOrderByNomAsc();
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualTo(users);
    }

    @Test
    public void save_nominal() {
        User user = mock(User.class);
        given(userRepository.save(user)).willReturn(user);

        User resultat = userService.save(user);

        then(userRepository).should().save(user);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(user);
    }

    @Test
    public void delete_nominal() {
        userService.delete(ID);

        then(userRepository).should().delete(ID);
    }

    @Test
    public void get_nominal() {
        User user = mock(User.class);
        given(userRepository.findOne(ID)).willReturn(user);

        User resultat = userService.get(ID);

        then(userRepository).should().findOne(ID);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(user);
    }

    @Test
    public void get_UserNonTrouve() {
        given(userRepository.findOne(ID)).willReturn(null);

        Throwable thrown = catchThrowable(() -> userService.get(ID));

        then(userRepository).should().findOne(ID);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void get_IdNull() {
        Throwable thrown = catchThrowable(() -> userService.get(null));

        then(userRepository).should(never()).findOne(any(Long.class));
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByEmail_nominal() {
        User user = mock(User.class);
        given(userRepository.findByEmail(EMAIL)).willReturn(user);

        User resultat = userService.findByEmail(EMAIL);

        then(userRepository).should().findByEmail(EMAIL);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(user);
    }

    @Test
    public void loadUserByUsername_nominal() {
        User user = mock(User.class);
        given(userRepository.findByEmail(EMAIL)).willReturn(user);

        UserDetails resultat = userService.loadUserByUsername(EMAIL);

        then(userRepository).should().findByEmail(EMAIL);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(user);
    }

    @Test
    public void loadUserByUsername_UserNonTrouve() {
        given(userRepository.findByEmail(EMAIL)).willReturn(null);

        Throwable thrown = catchThrowable(() -> userService.loadUserByUsername(EMAIL));

        then(userRepository).should().findByEmail(EMAIL);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void encodePassword_nominal() {
        User user = spy(User.class);
        user.setPassword(PASSWORD);

        userService.encodePassword(user);

        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getPassword()).isNotEmpty();
    }

    @Test
    public void setUserRole_nominal() {
        User user = spy(User.class);
        Role role = mock(Role.class);
        given(roleService.findByRole(ROLE_USER)).willReturn(role);

        userService.setUserRole(user);

        then(roleService).should().findByRole(ROLE_USER);
        assertThat(user.getRoles()).contains(role);
    }
}