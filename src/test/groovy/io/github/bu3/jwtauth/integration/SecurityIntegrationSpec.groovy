package io.github.bu3.jwtauth.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import spock.lang.Specification

import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityIntegrationSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "Should return forbidden if user has no right to access resources"() {
        when:
        ResponseEntity response = restTemplate.postForEntity("/users/sign-up", ["username": "foo", "password": "1234"], Void)

        then:
        response.statusCode == OK

        when:
        response = restTemplate.postForEntity("/login", ["username": "foo", "password": "1234"], Void)

        then:
        response.statusCode == OK
        def authHeader = response.headers.get("Authorization")[0]

        when:
        MultiValueMap<String, String> headers = new HttpHeaders()
        headers.put("Authorization", [authHeader])
        response = restTemplate.exchange("/api/tasks", HttpMethod.GET, new HttpEntity(headers), Void)

        then:
        response.statusCode == FORBIDDEN
    }

    def "Should return OK if user has right to access resources"() {
        when:
        def response = restTemplate.postForEntity("/login", ["username": "admin", "password": "1234"], Void)

        then:
        response.statusCode == OK
        def authHeader = response.headers.get("Authorization")[0]

        when:
        MultiValueMap<String, String> headers = new HttpHeaders()
        headers.put("Authorization", [authHeader])
        response = restTemplate.exchange("/api/tasks", HttpMethod.GET, new HttpEntity(headers), Void)

        then:
        response.statusCode == OK
    }
}

