package com.example.courseapi.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

/**
 * An interface for JWT related operations, providing methods for extracting username, generating and validating JWT tokens, and extracting claims.
 */
public interface JwtService {

    /**
     * Extracts the username from a JWT token.
     *
     * @param jwtToken the JWT token
     * @return the username extracted from the token
     */
    String extractUsername(final String jwtToken);

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails   the user details object containing user information
     * @param isAccessToken a boolean flag indicating whether the token is an access token or not
     * @return the generated JWT token
     */
    String generateJwtToken(final UserDetails userDetails, final boolean isAccessToken);

    /**
     * Generates a JWT token for the given user details and extra claims.
     *
     * @param userDetails   the user details object containing user information
     * @param extraClaims   a map containing extra claims to be included in the token
     * @param isAccessToken a boolean flag indicating whether the token is an access token or not
     * @return the generated JWT token with extra claims
     */
    String generateJwtToken(
            final UserDetails userDetails, final Map<String, Object> extraClaims, final boolean isAccessToken);

    /**
     * Validates a JWT token for the given user details.
     *
     * @param jwtToken    the JWT token to be validated
     * @param userDetails the user details object containing user information
     * @return true if the token is valid for the user, false otherwise
     */
    boolean isJwtTokenValid(final String jwtToken, final UserDetails userDetails);

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param token          the JWT token to extract the claim from
     * @param claimsResolver the resolver function to extract the claim
     * @param <T>            the type of the claim value
     * @return the extracted claim value
     */
    <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver);
}
