package com.example.bookwonders.service.impl;

import com.example.bookwonders.model.Role;
import com.example.bookwonders.model.RoleName;
import com.example.bookwonders.repository.user.RoleRepository;
import com.example.bookwonders.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(() ->
                new RuntimeException("can't find role by roleName: " + roleName));
    }
}
