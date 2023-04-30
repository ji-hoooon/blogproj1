package com.mino.blogproj.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUID_test {
    @Test
    public void uuid_test(){
        UUID uuid2 = UUID.randomUUID();
        System.out.println(uuid2);
        //난수를 반환한다.
    }
}
