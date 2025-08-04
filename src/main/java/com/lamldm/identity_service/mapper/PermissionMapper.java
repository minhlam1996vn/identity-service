package com.lamldm.identity_service.mapper;

import com.lamldm.identity_service.dto.request.PermissionRequest;
import com.lamldm.identity_service.dto.response.PermissionResponse;
import com.lamldm.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
