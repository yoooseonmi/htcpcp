package htcpcp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CoffeeHandler implements HttpHandler {

    private final CoffeePot pot;

    public CoffeeHandler(CoffeePot pot) {
        this.pot = pot;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "BREW" -> brew(exchange);
            case "GET" -> send(exchange, 200, pot.state().name());
            default -> {
                exchange.getResponseHeaders().set("Allow", "GET, BREW");
                send(exchange, 405, "I don't know");
            }
        }
    }

    private void brew(HttpExchange exchange) throws IOException {
        String command = readBody(exchange);
        System.out.println("command: "+ command);
        switch (command) {
            case "start" -> {
                if (pot.start()) send(exchange, 200, "추출 시작");
                else send(exchange, 409, "이미 추출 중입니다.");
            }
            case "stop" -> {
                if(pot.stop()) send(exchange, 200, "추출 중지");
                else send(exchange, 409, "추출 중이 아닙니다.");
            }
            default ->  send(exchange, 400, "상태 요청은 start, stop만 요청 가능합니다.");
        }
    }

    // 요청 본문: 바이트 -> 문자열
    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).strip();
        }
    }

    // 응답: 문자열 -> 바이트
    public void send(HttpExchange exchange, int status, String body) throws IOException{
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}