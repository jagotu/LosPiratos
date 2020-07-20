package com.vztekoverflow.lospiratos.webapp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Auth {
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String getLogin(String postData)
    {
        Map<String, List<String>> params = Helpers.splitUrlEncodedParams(postData);

        Helpers.assertPresentOnce(params, "username", "password");
        if(!verifyLogin(params.get("username").get(0), params.get("password").get(0))) {
            throw new WebAppServer.UnauthorizedException("Invalid login"); //TODO: are exceptions too slow for wrong passwords?
        }
        return params.get("username").get(0); //TODO: return differently
    }

    private static boolean verifyLogin(String username, String password)
    {
        //TODO
        return password.equals("a");
    }


    public static String getSecretForTeam(String id) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] key = getKey();
        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(key, HMAC_SHA512);
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(id.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(macData);
    }

    public static void assertAuthTeam(String id, String secret) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        if(!authTeam(id, secret))
            throw new WebAppServer.UnauthorizedException("Invalid team token");
    }

    private static boolean authTeam(String id, String secret) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(secret == null || secret.isEmpty())
        {
            return false;
        }
        byte[] key = getKey();
        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(key, HMAC_SHA512);
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(id.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(macData).equals(secret);
    }

    private static byte[] getKey() throws IOException {
        File f = new File("secret.key");
        if(!f.exists() || !f.canRead())
        {
            System.out.println("Web server secret.key not found, generating one...");
            genKey();
        }

        return Files.readAllBytes(f.toPath());
    }

    private static void genKey() throws IOException {
        Random rd = new Random();
        byte[] arr = new byte[32];
        rd.nextBytes(arr);

        byte[] key = Base64.getEncoder().encode(arr);
        Files.write(new File("secret.key").toPath(), key, StandardOpenOption.CREATE_NEW);

    }

}
