package vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HTTPVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new HTTPVerticle());
  }

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

