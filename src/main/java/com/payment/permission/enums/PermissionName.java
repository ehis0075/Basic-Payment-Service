package com.payment.permission.enums;

public enum PermissionName {
    MAKE_PAYMENT(PermissionType.MERCHANT), VIEW_TRANSACTION_PAYMENT_HISTORY(PermissionType.MERCHANT),
    CREATE_USER(PermissionType.MERCHANT), VIEW_USER(PermissionType.MERCHANT);
    public final PermissionType permissionType;

    PermissionName(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
