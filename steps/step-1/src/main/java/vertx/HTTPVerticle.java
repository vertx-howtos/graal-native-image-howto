package vertx;

import io.vertx.core.AbstractVerticle;

public class HTTPVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8080, listen -> {
      if (listen.succeeded()) {
        System.out.println("Server listening on http://localhost:8080/");
      } else {
        listen.cause().printStackTrace();
        System.exit(1);
      }
    });
  }
}

