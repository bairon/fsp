package com.alsa;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by alsa on 03.11.2016.
 */

public final class Utils {
    public static String plusOneBase36(String base36) {
        long base = Long.parseLong(base36, 36);
        return Long.toString(base + 1, 36);
    }

    public static void withRole(String ... role) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("", "", AuthorityUtils.createAuthorityList(role)));
    }

    public static void clearRole() {
        SecurityContextHolder.clearContext();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
