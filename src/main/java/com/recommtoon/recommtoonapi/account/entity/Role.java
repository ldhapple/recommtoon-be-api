package com.recommtoon.recommtoonapi.account.entity;

public enum Role {

    NOT_SIGNED, USER, ADMIN

    public static Role from(String s) {
        return Role.valueOf(s.toUpperCase());
    }
}
