package com.lucas.exportNfsMei.model;

public record RequestCookiesNfse(
        String arraffinity,
        String sessionId,
        String emissor,
        String requestVerificationToken
){}
