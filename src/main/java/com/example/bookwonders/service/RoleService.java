package com.example.bookwonders.service;

import com.example.bookwonders.model.Role;
import com.example.bookwonders.model.RoleName;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
