package org.webswing.server.common.util;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.model.security.*;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.WebswingLoginSessionTokenClaim;
import org.webswing.server.common.service.security.WebswingTokenClaim;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class JwtUtil {

  private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

  private static final byte[] signingKey;
  private static final HmacKey hmacKey;
  private static final int MINIMUM_SIGNING_KEY_BYTES = 32; // 256-bit minimum for HMAC-SHA256

  // AES-GCM provides authenticated encryption (confidentiality + integrity),
  // replacing AES-CBC which was vulnerable to padding oracle attacks
  private static final String encryptionAlg = "AES/GCM/NoPadding";
  private static final int GCM_IV_LENGTH = 12; // 96-bit IV recommended for GCM
  private static final int GCM_TAG_LENGTH = 128; // 128-bit authentication tag
  private static final String encryptionKeySpec = "AES";
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  /**
   * Maximum allowed size for decompressed claim data (1 MB). Prevents decompression bomb attacks
   * where a small compressed payload expands to consume all available memory.
   */
  private static final int MAX_DECOMPRESSED_SIZE = 1024 * 1024;

  private static final boolean usegGzip =
      Boolean.parseBoolean(System.getProperty(Constants.JWT_SERIALIZATION_USE_GZIP,
          Constants.JWT_SERIALIZATION_USE_GZIP_DEFAULT));
  private static final boolean useProto =
      Boolean.parseBoolean(System.getProperty(Constants.JWT_SERIALIZATION_USE_PROTO,
          Constants.JWT_SERIALIZATION_USE_PROTO_DEFAULT));
  private static final boolean useEncryption =
      Boolean.parseBoolean(System.getProperty(Constants.jWT_SERIALIZATION_USE_ENCRYPTION,
          Constants.JWT_SERIALIZATION_USE_ENCRYPTION_DEFAULT));

  private static final ProtoMapper protoMapper =
      new ProtoMapper(ProtoMapper.PROTO_PACKAGE_JWT, ProtoMapper.PROTO_PACKAGE_JWT);
  private static final JsonMapper mapper = new JsonMapper();

  private static SecretKey secretKey;

  static {
    // Validate signing key length at startup
    byte[] rawKey =
        System.getProperty(Constants.WEBSWING_CONNECTION_SECRET).getBytes(StandardCharsets.UTF_8);
    if (rawKey.length < MINIMUM_SIGNING_KEY_BYTES) {
      log.error(
          "JWT signing key is only {} bytes — minimum {} bytes required for HMAC-SHA256. "
              + "Set a longer value for {}.",
          rawKey.length, MINIMUM_SIGNING_KEY_BYTES, Constants.WEBSWING_CONNECTION_SECRET);
      throw new IllegalStateException("JWT signing key too short: " + rawKey.length
          + " bytes, minimum " + MINIMUM_SIGNING_KEY_BYTES + " required");
    }
    signingKey = rawKey;
    hmacKey = new HmacKey(signingKey);

    try {
      secretKey = new SecretKeySpec(Arrays.copyOfRange(signingKey, 0, 32), encryptionKeySpec);
    } catch (Exception e) {
      log.error("Failed to initialize JWT encryption!", e);
    }
  }

  // ── Token creation ──────────────────────────────────────────────────

  public static String createHandshakeToken() {
    return buildToken(
        Long.getLong(Constants.JWT_HANDSHAKE_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_HANDSHAKE_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_HANDSHAKE, null, null);
  }

  public static String createAccessToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_ACCESS_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_ACCESS_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_ACCESS, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createRefreshToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_REFRESH_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_REFRESH_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_REFRESH, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createLoginSessionToken(String webswingLoginSessionClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_LOGIN_SESSION_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_LOGIN_SESSION_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_LOGIN_SESSION, Constants.JWT_CLAIM_WEBSWING_LOGIN_SESSION,
        webswingLoginSessionClaim);
  }

  public static String createTransferToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_TRANSFER_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_TRANSFER_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_TRANSFER, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createAdminConsoleLoginToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_ADMIN_CONSOLE_LOGIN_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_ADMIN_CONSOLE_LOGIN_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_ADMIN_CONSOLE_LOGIN, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createAdminConsoleAccessToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_ADMIN_CONSOLE_ACCESS_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_ADMIN_CONSOLE_ACCESS_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_ADMIN_CONSOLE_ACCESS, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createAdminConsoleRefreshToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_ADMIN_CONSOLE_REFRESH_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_ADMIN_CONSOLE_REFRESH_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_ADMIN_CONSOLE_REFRESH, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  public static String createAdminConsoleDownloadToken(String webswingClaim) {
    return buildToken(
        Long.getLong(Constants.JWT_ADMIN_CONSOLE_DOWNLOAD_TOKEN_EXPIRATION_MILLIS,
            Constants.JWT_ADMIN_CONSOLE_DOWNLOAD_TOKEN_EXPIRATION_MILLIS_DEFAULT),
        Constants.JWT_SUBJECT_ADMIN_CONSOLE_DOWNLOAD, Constants.JWT_CLAIM_WEBSWING, webswingClaim);
  }

  private static String buildToken(long expirationMillis, String subject, String claimKey,
      String claimValue) {
    try {
      long now = System.currentTimeMillis();

      JwtClaims claims = new JwtClaims();
      claims.setSubject(subject);
      claims.setIssuedAt(NumericDate.fromMilliseconds(now));
      claims.setExpirationTime(NumericDate.fromMilliseconds(now + expirationMillis));
      claims.setJwtId(UUID.randomUUID().toString());
      if (claimKey != null) {
        claims.setStringClaim(claimKey, claimValue);
      }

      JsonWebSignature jws = new JsonWebSignature();
      jws.setPayload(claims.toJson());
      jws.setKey(hmacKey);
      jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
      return jws.getCompactSerialization();
    } catch (Exception e) {
      log.error("Failed to create JWT token for subject [{}]!", subject, e);
      return null;
    }
  }

  // ── Token validation ────────────────────────────────────────────────

  public static boolean validateHandshakeToken(String token) {
    return validateToken(token, Constants.JWT_SUBJECT_HANDSHAKE);
  }

  private static boolean validateToken(String token, String subjectExpected) {
    try {
      createTokenConsumer(subjectExpected).processToClaims(token);
      return true;
    } catch (Exception e) {
      // Do not log the token value — it may contain sensitive claims
      // and could be replayed if logs are compromised
      log.debug("Could not validate JWT token for subject [{}]!", subjectExpected, e);
    }
    return false;
  }

  // ── Token parsing — returns Map<String, Object> ─────────────────────
  // Callers never see jose4j types — only plain Java maps.

  public static Map<String, Object> parseAccessTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_ACCESS);
  }

  public static Map<String, Object> parseRefreshTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_REFRESH);
  }

  public static Map<String, Object> parseLoginSessionTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_LOGIN_SESSION);
  }

  public static Map<String, Object> parseTransferTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_TRANSFER);
  }

  public static Map<String, Object> parseAdminConsoleLoginTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_ADMIN_CONSOLE_LOGIN);
  }

  public static Map<String, Object> parseAdminConsoleAccessTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_ADMIN_CONSOLE_ACCESS);
  }

  public static Map<String, Object> parseAdminConsoleRefreshTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_ADMIN_CONSOLE_REFRESH);
  }

  public static Map<String, Object> parseAdminConsoleDownloadTokenClaims(String token) {
    return parseTokenClaims(token, Constants.JWT_SUBJECT_ADMIN_CONSOLE_DOWNLOAD);
  }

  private static Map<String, Object> parseTokenClaims(String token, String subjectExpected) {
    try {
      JwtClaims claims = createTokenConsumer(subjectExpected).processToClaims(token);
      return claims.getClaimsMap();
    } catch (Exception e) {
      // Do not log the token value to prevent credential leakage in logs
      log.debug("Could not validate and parse claims from JWT token for subject [{}]!",
          subjectExpected, e);
    }
    return null;
  }

  private static JwtConsumer createTokenConsumer(String subjectExpected) {
    return new JwtConsumerBuilder().setRequireSubject().setExpectedSubject(subjectExpected)
        .setRequireExpirationTime().setRequireIssuedAt()
        .setAllowedClockSkewInSeconds(
            Long.getLong(Constants.JWT_CLOCK_SKEW_SECONDS, Constants.JWT_CLOCK_SKEW_SECONDS_DEFAULT)
                .intValue())
        .setVerificationKey(hmacKey).build();
  }

  // ── Claim serialization (unchanged — no jjwt dependency) ────────────

  public static String serializeWebswingClaim(WebswingTokenClaim webswingClaim) {
    // serialize (json/protobuf)
    // gzip
    // encrypt

    byte[] serialized = null;

    if (useProto) {
      try {
        serialized = protoMapper.encodeProto(new WebswingTokenClaimProto(webswingClaim));
      } catch (IOException e) {
        log.error("Failed to serialize user map!", e);
      }
    } else {
      try {
        serialized = mapper.writeValueAsBytes(webswingClaim);
      } catch (JacksonException e) {
        log.error("Failed to serialize user map!", e);
      }
    }

    if (serialized == null) {
      log.error("Serialization produced null — cannot create claim");
      return null;
    }

    return compressAndEncryptWebswingClaim(serialized);
  }

  public static String serializeWebswingLoginSessionClaim(
      WebswingLoginSessionTokenClaim loginSessionClaim) {
    // serialize (json/protobuf)
    // gzip
    // encrypt

    byte[] serialized = null;

    if (useProto) {
      try {
        serialized =
            protoMapper.encodeProto(new WebswingLoginSessionTokenClaimProto(loginSessionClaim));
      } catch (IOException e) {
        log.error("Failed to serialize user map!", e);
      }
    } else {
      try {
        serialized = mapper.writeValueAsBytes(loginSessionClaim);
      } catch (JacksonException e) {
        log.error("Failed to serialize user map!", e);
      }
    }

    if (serialized == null) {
      log.error("Serialization produced null — cannot create login session claim");
      return null;
    }

    return compressAndEncryptWebswingClaim(serialized);
  }

  private static String compressAndEncryptWebswingClaim(byte[] serializedWebswingClaim) {
    if (serializedWebswingClaim == null) {
      return null;
    }

    byte[] claimBytes = serializedWebswingClaim;

    if (usegGzip) {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
          GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
        gzip.write(claimBytes);
        gzip.finish();
        claimBytes = baos.toByteArray();
      } catch (IOException e) {
        log.error("Could not gzip token claim!", e);
      }
    }

    if (useEncryption) {
      if (secretKey == null) {
        return null;
      }
      try {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(encryptionAlg);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        byte[] encrypted = cipher.doFinal(claimBytes);
        // Prepend IV to encrypted data (GCM tag is appended by the cipher)
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        claimBytes = Base64.getUrlEncoder().encode(combined);
      } catch (Exception e) {
        log.error("Failed to encrypt user map for JWT token!", e);
      }
    }

    return new String(claimBytes, StandardCharsets.UTF_8);
  }

  public static WebswingTokenClaim deserializeWebswingClaim(String webswingClaim)
      throws IOException {
    // decrypt
    // ungzip
    // deserialize (json/protobuf)

    byte[] serialized = decryptAndDecompressWebswingClaim(webswingClaim);

    if (serialized == null) {
      throw new IOException("Failed to decrypt/decompress webswing claim");
    }

    if (useProto) {
      WebswingTokenClaimProto claimProto =
          protoMapper.decodeProto(serialized, WebswingTokenClaimProto.class);

      WebswingTokenClaim claim = new WebswingTokenClaim();
      claim.setHost(claimProto.getHost());
      if (claimProto.getAttributes() != null) {
        Map<String, Object> attributes = new HashMap<>();
        for (MapProto entry : claimProto.getAttributes()) {
          try {
            attributes.put(entry.getKey(), mapper.readValue(entry.getValue(), Object.class));
          } catch (Exception e) {
            log.error("Could not deserialize attribute [{}]!", sanitizeForLog(entry.getKey()), e);
          }
        }
        claim.setAttributes(attributes);
      }
      if (claimProto.getUserMap() != null) {
        Map<String, AbstractWebswingUser> userMap = new HashMap<>();
        for (AbstractWebswingUserProto user : claimProto.getUserMap()) {
          AbstractWebswingUser awu = new AbstractWebswingUser();
          awu.setUserId(user.getUserId());
          awu.setRoles(user.getRoles());

          List<String> permissions = new ArrayList<>();
          if (user.getPermissions() != null) {
            // BUG FIX: was permissions.addAll(permissions) — a self-add no-op
            permissions.addAll(user.getPermissions());
          }
          if (user.getWebswingActionPermissions() != null) {
            for (WebswingAction wa : user.getWebswingActionPermissions()) {
              permissions.add(wa.name());
            }
          }
          awu.setPermissions(permissions);
          if (user.getUserAttributes() != null) {
            Map<String, Serializable> userAttributes = new HashMap<>();
            for (MapProto entry : user.getUserAttributes()) {
              try {
                userAttributes.put(entry.getKey(),
                    mapper.readValue(entry.getValue(), Serializable.class));
              } catch (Exception e) {
                log.error("Could not deserialize attribute [{}]!", sanitizeForLog(entry.getKey()),
                    e);
              }
            }
            awu.setUserAttributes(userAttributes);
          }

          userMap.put(user.getSecuredPath(), awu);
        }
        claim.setUserMap(userMap);
      }

      return claim;
    } else {
      try {
        return mapper.readValue(serialized, WebswingTokenClaim.class);
      } catch (JacksonException e) {
        log.error("Failed to deserialize user map!", e);
        throw new IOException("Failed to deserialize user map", e);
      }
    }
  }

  public static WebswingLoginSessionTokenClaim deserializeWebswingLoginSessionClaim(
      String loginSessionClaim) throws IOException {
    // decrypt
    // ungzip
    // deserialize (json/protobuf)

    byte[] serialized = decryptAndDecompressWebswingClaim(loginSessionClaim);

    if (serialized == null) {
      throw new IOException("Failed to decrypt/decompress login session claim");
    }

    if (useProto) {
      WebswingLoginSessionTokenClaimProto claimProto =
          protoMapper.decodeProto(serialized, WebswingLoginSessionTokenClaimProto.class);

      WebswingLoginSessionTokenClaim claim = new WebswingLoginSessionTokenClaim();
      if (claimProto.getAttributes() != null) {
        Map<String, Object> attributes = new HashMap<>();
        for (MapProto entry : claimProto.getAttributes()) {
          try {
            attributes.put(entry.getKey(), mapper.readValue(entry.getValue(), Object.class));
          } catch (Exception e) {
            log.error("Could not deserialize attribute [{}]!", sanitizeForLog(entry.getKey()), e);
          }
        }
        claim.setAttributes(attributes);
      }

      return claim;
    } else {
      try {
        return mapper.readValue(serialized, WebswingLoginSessionTokenClaim.class);
      } catch (JacksonException e) {
        log.error("Failed to deserialize user map!", e);
        throw new IOException("Failed to deserialize user map", e);
      }
    }
  }

  private static byte[] decryptAndDecompressWebswingClaim(String serializedWebswingClaim) {
    if (serializedWebswingClaim == null) {
      return null;
    }

    byte[] claimBytes = serializedWebswingClaim.getBytes(StandardCharsets.UTF_8);

    if (useEncryption) {
      if (secretKey == null) {
        return null;
      }

      try {
        byte[] decoded = Base64.getUrlDecoder().decode(claimBytes);
        if (decoded.length < GCM_IV_LENGTH) {
          log.error("Encrypted claim data too short — expected at least {} bytes for IV",
              GCM_IV_LENGTH);
          return null;
        }
        // Extract IV (first 12 bytes for GCM)
        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
        byte[] encrypted = Arrays.copyOfRange(decoded, GCM_IV_LENGTH, decoded.length);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(encryptionAlg);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        claimBytes = cipher.doFinal(encrypted);
      } catch (Exception e) {
        log.error("Failed to decrypt user map for JWT token!", e);
        return null;
      }
    }

    if (usegGzip) {
      try (ByteArrayInputStream bais = new ByteArrayInputStream(claimBytes);
          GZIPInputStream gzip = new GZIPInputStream(bais);
          ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        // Bounded decompression to prevent decompression bomb (zip bomb) attacks
        byte[] buffer = new byte[8192];
        int totalRead = 0;
        int n;
        while ((n = gzip.read(buffer)) >= 0) {
          totalRead += n;
          if (totalRead > MAX_DECOMPRESSED_SIZE) {
            log.error(
                "Decompressed claim exceeds maximum allowed size of {} bytes — possible decompression bomb",
                MAX_DECOMPRESSED_SIZE);
            return null;
          }
          baos.write(buffer, 0, n);
        }
        claimBytes = baos.toByteArray();
      } catch (IOException e) {
        log.error("Could not un-gzip token claim!", e);
        return null;
      }
    }

    return claimBytes;
  }

  /**
   * Sanitize a string for safe inclusion in log messages.
   */
  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    return input.replaceAll("[\\r\\n\\t]", "_");
  }

  private JwtUtil() {}

}
