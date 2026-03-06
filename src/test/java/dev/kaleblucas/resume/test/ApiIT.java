package dev.kaleblucas.resume.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.config.TestConfig;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiIT extends BaseIT {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient http = HttpClient.newHttpClient();

    private JsonNode get(String path) throws Exception {
        HttpResponse<String> resp = http.send(
            HttpRequest.newBuilder()
                .uri(URI.create(TestConfig.baseUrl() + path))
                .GET().build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertThat(resp.statusCode()).as("HTTP status for " + path).isEqualTo(200);
        return mapper.readTree(resp.body());
    }

    @Test
    public void skillsEndpointReturnsFourCategories() throws Exception {
        JsonNode body = get("/api/skills");
        assertThat(body.isArray()).isTrue();
        assertThat(body.size()).isEqualTo(4);
    }

    @Test
    public void skillsEndpointFirstCategoryIsTestAutomation() throws Exception {
        JsonNode body = get("/api/skills");
        assertThat(body.get(0).get("name").asText()).isEqualTo("Test Automation");
    }

    @Test
    public void skillCategoriesHaveNonEmptySkillsArray() throws Exception {
        JsonNode body = get("/api/skills");
        for (JsonNode category : body) {
            assertThat(category.has("name")).isTrue();
            assertThat(category.get("skills").isArray()).isTrue();
            assertThat(category.get("skills").size()).isGreaterThan(0);
        }
    }

    @Test
    public void experienceEndpointReturnsThreeEntries() throws Exception {
        JsonNode body = get("/api/experience");
        assertThat(body.isArray()).isTrue();
        assertThat(body.size()).isEqualTo(3);
    }

    @Test
    public void experienceEndpointFirstEntryIsQuavo() throws Exception {
        JsonNode body = get("/api/experience");
        assertThat(body.get(0).get("company").asText()).isEqualTo("Quavo Fraud & Disputes");
    }

    @Test
    public void experienceEntriesHaveAllRequiredFields() throws Exception {
        JsonNode body = get("/api/experience");
        for (JsonNode entry : body) {
            assertThat(entry.has("company")).isTrue();
            assertThat(entry.has("role")).isTrue();
            assertThat(entry.has("tenure")).isTrue();
            assertThat(entry.has("location")).isTrue();
            assertThat(entry.get("bullets").isArray()).isTrue();
            assertThat(entry.get("tech").isArray()).isTrue();
        }
    }
}
