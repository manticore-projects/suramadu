package org.webswing.server.services.security.modules.embeded;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HashUtil {

  public static final String PREFIX = "$hashed$";

  public static final int COST = 16;

  private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

  private static final int SIZE = 128;

  private static final Pattern layout = Pattern.compile("\\$hashed\\$(.{43})");

  private static final SecureRandom random = new SecureRandom();;

  public static String hash(char[] password) {
    byte[] salt = new byte[SIZE / 8];
    random.nextBytes(salt);
    byte[] dk = pbkdf2(password, salt, COST);
    byte[] hash = new byte[salt.length + dk.length];
    System.arraycopy(salt, 0, hash, 0, salt.length);
    System.arraycopy(dk, 0, hash, salt.length, dk.length);
    Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
    return PREFIX + enc.encodeToString(hash);
  }

  public static boolean authenticate(char[] password, String token) {
    Matcher m = layout.matcher(token);
    if (!m.matches()) {
      throw new IllegalArgumentException("Invalid token format");
    }
    byte[] hash = Base64.getUrlDecoder().decode(m.group(1));
    byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
    byte[] check = pbkdf2(password, salt, COST);
    int zero = 0;
    for (int idx = 0; idx < check.length; idx++) {
      zero |= hash[salt.length + idx] ^ check[idx];
    }
    return zero == 0;
  }

  private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
    KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
    try {
      SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
      return f.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
    } catch (InvalidKeySpecException ex) {
      throw new IllegalStateException("Invalid SecretKeyFactory", ex);
    }
  }

  private HashUtil() {}
}
