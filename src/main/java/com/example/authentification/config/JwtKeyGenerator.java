package com.example.authentification.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // 🔑 Génère une clé sécurisée pour HS256
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 🔐 Encodez la clé en Base64 pour la stocker facilement
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Clé secrète (Base64) : " + base64Key);
    }
}