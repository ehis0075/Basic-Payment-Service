package com.payment.permission.enums;

public enum PermissionName {
    CREATE_USER(PermissionType.USER), VIEW_USER(PermissionType.SUPER),
    VIEW_PERMISSION(PermissionType.SUPER),
    MAKE_PAYMENT(PermissionType.USER);
    public final PermissionType permissionType;
    PermissionName(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
