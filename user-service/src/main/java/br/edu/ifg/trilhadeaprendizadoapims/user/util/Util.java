package br.edu.ifg.trilhadeaprendizadoapims.user.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    public static String gerarHashMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash MD5", e);
        }
    }
}