package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.domain.Role;
import com.cmarchive.bank.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {
    private static final String ROLE = "ROLE";

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void get_nominal() {
        Role role = mock(Role.class);
        given(roleRepository.findByRole(ROLE)).willReturn(role);

        Role resultat = roleService.get(ROLE);

        then(roleRepository).should().findByRole(ROLE);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(role);
    }

    @Test
    public void findByRole_nominal() {
        Role role = mock(Role.class);
        given(roleRepository.findByRole(ROLE)).willReturn(role);

        Role resultat = roleService.get(ROLE);

        then(roleRepository).should().findByRole(ROLE);
        assertThat(resultat).isNotNull();
        assertThat(resultat).isEqualToComparingFieldByField(role);
    }
}