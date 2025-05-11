package com.rf.sorocaba.demo.core.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EncryptDecryptUtilsTest {

    @Autowired
    private EncryptDecryptUtils encryptDecryptUtils;

    @Test
    public void encryptTest(){
        String rawText = "test";

        String result = encryptDecryptUtils.encrypt(rawText);

        assertNotNull(result);
        assertNotEquals(rawText, result);
        assertEquals(rawText, encryptDecryptUtils.decrypt(result));
    }

}
