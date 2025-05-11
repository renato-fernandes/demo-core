package com.rf.sorocaba.demo.core.utils;

import jakarta.annotation.PostConstruct;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class EncryptDecryptUtils {

    private BasicTextEncryptor stringEncryptor;

    @Value("${jasypt.encryptor.password}")
    private String encryptName;

    public EncryptDecryptUtils(){
    }

    @PostConstruct
    private void initEncryptor(){
        stringEncryptor = new BasicTextEncryptor();
        stringEncryptor.setPasswordCharArray(encryptName.toCharArray());
    }

    public String encrypt(String rawText){
        return stringEncryptor.encrypt(rawText);
    }

    public String decrypt(String encryptedText){
        return stringEncryptor.decrypt(encryptedText);
    }

}
