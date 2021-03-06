package com.eduardosmatheus.githubtagsserver.model.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubUser(val login: String, val avatar_url: String, val email: String, val name: String)