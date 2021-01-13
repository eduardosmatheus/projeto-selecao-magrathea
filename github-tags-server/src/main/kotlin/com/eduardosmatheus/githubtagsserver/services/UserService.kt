package com.eduardosmatheus.githubtagsserver.services

import com.eduardosmatheus.githubtagsserver.security.UserClaims
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UserService {

    private val githubBaseURL = "https://github.com/login/oauth/access_token"
    private val client_id = "b34d2a6fc9da4d853f0a"
    private val client_secret = "bf7914d3bce7e9a5cb6ec9eb9126cc6eeafc8bc3"
    private val BAD_VERIFICATION_CODE = "bad_verification_code"

    fun getClaims(code: String): ResponseEntity<Any> {
        val (client, entity) = githubClient()
        val response = getGithubAuthorization(client, code, entity)
        val errorCode = response["error"]
        if (errorCode != null && errorCode.asText() == BAD_VERIFICATION_CODE) {
            return ResponseEntity(response, HttpStatus.FORBIDDEN)
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun githubClient(): Pair<RestTemplate, HttpEntity<UserClaims>> {
        val client = RestTemplate()
        val headers = HttpHeaders()
        headers["Accept"] = "application/json"
        val entity = HttpEntity<UserClaims>(headers)
        return Pair(client, entity)
    }

    private fun getGithubAuthorization(
        client: RestTemplate,
        code: String,
        entity: HttpEntity<UserClaims>
    ): JsonNode {
        val response = client.exchange(
            "$githubBaseURL?client_id=${client_id}&client_secret=${client_secret}&code=$code",
            HttpMethod.POST,
            entity,
            JsonNode::class.java
        )
        return response.body!!
    }

}