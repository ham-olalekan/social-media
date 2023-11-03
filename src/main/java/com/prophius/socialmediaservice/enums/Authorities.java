package com.prophius.socialmediaservice.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public enum Authorities {
    USER, API_USER;

    public static final String USER_PREAUTHORIZE = "hasAuthority('USER')";

    public static Authorities[] DEFAULT(Authorities... authorities) {
        HashSet<Authorities> authoritiesHashSet = new HashSet<>();
        authoritiesHashSet.add(USER);
        authoritiesHashSet.addAll(Arrays.stream(authorities).collect(Collectors.toSet()));
        return authoritiesHashSet.toArray(new Authorities[]{});
    }
}
