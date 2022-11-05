package com.akai.hackathon.database;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import lombok.experimental.FieldDefaults;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;

@Component
public class SessionManager {

    HashMap<String, Session> sessions = new HashMap<>();

    public String checkAuthorization(String token) {
        return Optional.ofNullable(sessions.get(token))
                .map( session -> {
                    if( session.getExpireTime().isBefore( Instant.now())) {
                        sessions.remove(token);
                        throw new TokenExpiredException();
                    }
                    return session.getName();
                }).orElseThrow(TokenNotFoundException::new);
    }

    public void addSession(Session session) {
        sessions.put(session.getToken(), session);
    }

    public Session getSession(String user) {
        return sessions.values()
                .stream()
                .filter( session -> session.getName().equals(user))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }

    public Session tokenRenewal(String name) {
        try {
            return Optional.of(getSession(name))
                    .map(session -> session.withExpireTime(Instant.now().plus(2, ChronoUnit.HOURS)))
                    .map(session -> {
                        sessions.replace(
                                session.getToken(),
                                session);
                        return session;
                    }).orElseGet(() -> {
                        String token = SHA3String(String.valueOf(Instant.now().toEpochMilli()));
                        Session session = Session.builder()
                                .name(name)
                                .token(SHA3String(String.valueOf(Instant.now().toEpochMilli())))
                                .expireTime(Instant.now().plus(2, ChronoUnit.HOURS))
                                .build();
                        sessions.putIfAbsent(
                                token,
                                session
                        );
                        return session;
                    });
        } catch (UserNotFoundException e) {
            Session session = Session.builder()
                    .name(name)
                    .token(SHA3String(String.valueOf(Instant.now().toEpochMilli())))
                    .expireTime(Instant.now().plus(2, ChronoUnit.HOURS))
                    .build();
            sessions.put(
                    session.getToken(),
                    session);
            return session;
        }
    }

    public static String SHA3String(String text) {
        SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
        sha3.update(text.getBytes());
        StringBuilder passwordBuilder = new StringBuilder();

        for (byte b : sha3.digest()) {
            passwordBuilder.append(String.format("%02x", b & 0xFF));
        }

        return passwordBuilder.toString();
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @With
    static public class Session {
        String token;
        String name;
        Instant expireTime;
    }

    public static abstract class SessionManagerException extends RuntimeException {}

    static class TokenExpiredException extends SessionManagerException {

        public TokenExpiredException() {
            super();
        }
    }

    static class UserNotFoundException extends SessionManagerException {

        public UserNotFoundException() {
            super();
        }
    }

    static public class TokenNotFoundException extends SessionManagerException {

        public TokenNotFoundException() {
            super();
        }
    }
}
