package org.hust.app.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class HashPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return ShaUtils.code( charSequence.toString(),ShaUtils.SHA_1);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(ShaUtils.code(charSequence .toString(),ShaUtils.SHA_1));
    }
}
