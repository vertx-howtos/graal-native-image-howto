
package vertx;

import static io.reactiverse.junit5.web.TestRequest.bodyResponse;
import static io.reactiverse.junit5.web.TestRequest.statusCode;
import static io.reactiverse.junit5.web.TestRequest.testRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.reactiverse.junit5.web.WebClientOptionsInject;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

/**
 * Tests of the native images created by this project.
 */
@ExtendWith(VertxExtension.class)
class HowtoVerticlesIT {

    /**
     * Expected log output.
     */
    private static final String EXPECTED_OUTPUT = "Server listening";

    /**
     * WebClient options for tests.
     */
    @WebClientOptionsInject
    public WebClientOptions options = new WebClientOptions().setDefaultHost("localhost").setTrustAll(true);

    /**
     * Tests the HTTPVerticle class.
     *
     * @param vertx A Vert.x instance
     * @param testContext A test context
     * @throws IOException If there is trouble reading from the test's process
     */
    @Test
    final void testHTTPVerticle(final Vertx vertx, final VertxTestContext testContext, final WebClient client)
            throws IOException {
        final Process process = getProcess("vertx.HTTPVerticle");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            testContext.verify(() -> {
                // Check that server started
                assertEquals(EXPECTED_OUTPUT, reader.readLine().substring(0, EXPECTED_OUTPUT.length()));
                // Check that it responds as expected
                testRequest(client, HttpMethod.GET, "/").with(request -> request.port(8080))
                        .expect(statusCode(200), bodyResponse(Buffer.buffer("Hello from Vert.x!"), "text/plain"))
                        .send(testContext, (VertxTestContext.ExecutionBlock) () -> {
                            process.destroyForcibly();
                            testContext.completeNow();
                        });
            });
        }
    }

    /**
     * Tests the HTTPSVerticle class.
     *
     * @param vertx A Vert.x instance
     * @param testContext A test context
     * @throws IOException If there is trouble reading from the test's process
     */
    @Test
    final void testHTTPSVerticle(final Vertx vertx, final VertxTestContext testContext, final WebClient client)
            throws IOException {
        final Process process = getProcess("vertx.HTTPSVerticle");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            testContext.verify(() -> {
                // Check that server started
                assertEquals(EXPECTED_OUTPUT, reader.readLine().substring(0, EXPECTED_OUTPUT.length()));
                // Check that it responds as expected
                testRequest(client, HttpMethod.GET, "/").with(request -> request.port(8443).ssl(true))
                        .expect(statusCode(200), bodyResponse(Buffer.buffer("Hello from Vert.x!"), "text/plain"))
                        .send(testContext, (VertxTestContext.ExecutionBlock) () -> {
                            process.destroyForcibly();
                            testContext.completeNow();
                        });
            });
        }
    }

    /**
     * Tests the output from APIClientVerticle.
     *
     * @param vertx A Vert.x instance
     * @param testContext A test context
     * @throws IOException If there is trouble reading from the test's process
     */
    @Test
    final void testAPIClientVerticle(final Vertx vertx, final VertxTestContext testContext) throws IOException {
        final Process process = getProcess("vertx.APIClientVerticle");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            final StringBuilder sb = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append(' ');
            }

            testContext.verify(() -> {
                process.destroyForcibly();
                assertTrue(sb.toString().contains("Got HTTP response with status 200"));
                testContext.completeNow();
            });
        }
    }

    /**
     * Gets a process for a particular test.
     *
     * @param test The test being run
     * @return A process
     * @throws IOException If there is trouble reading from the process
     */
    final private Process getProcess(final String test) throws IOException {
        return new ProcessBuilder("target/hello_native", "run", test).redirectErrorStream(true).start();
    }
}
