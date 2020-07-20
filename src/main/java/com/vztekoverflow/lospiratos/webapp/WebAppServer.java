package com.vztekoverflow.lospiratos.webapp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.viewmodel.Game;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WebAppServer implements HttpHandler {

    private HttpServer server = null;
    private Game game = null;

    public boolean isRunning() {
        return running;
    }

    private boolean running = false;


    public void start() throws IOException {

        if (game == null) {
            throw new IllegalStateException("No game.");
        }
        if (running) {
            throw new IllegalStateException("Server already running.");
        }

        server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/", this);
        server.setExecutor(Executors.newFixedThreadPool(10));

        server.start();
        running = true;

    }

    private byte[] currentjpg;
    private final Object jpglock = new Object();

    public void setCurrentMap(byte[] map) {
        synchronized (jpglock) {
            currentjpg = map;
        }
}

    public void setGame(Game g) {
        this.game = g;
    }

    public void stop(int delay) {
        if (!running) {
            throw new IllegalStateException("Server not running.");
        }

        server.stop(delay);
        running = false;
    }

    private String getTeamTokenValue(HttpExchange exchange)
    {
        String token = null;

        if(exchange.getRequestHeaders().containsKey("Cookie"))
        {
            for(String cookies : exchange.getRequestHeaders().get("Cookie"))
            {
                for(String cookie : cookies.split(";"))
                {
                    if(cookie.startsWith("teamToken="))
                    {
                        token = cookie.replace("teamToken=", "");
                    }
                }
            }
        }

        /*if(exchange.getRequestBody() != null)
        {
            try {
                String postData = new String(Helpers.readAllBytes(exchange.getRequestBody()), UTF_8);
                Map<String, List<String>> postParams = Helpers.splitUrlEncodedParams(postData);
                if(Helpers.isPresentOnce(postParams, "teamToken"))
                {
                    token = postParams.get("teamToken").get(0);
                }
            } catch (IOException ignored) {

            }
        }

        Map<String, List<String>> urlParams = Helpers.splitQuery(exchange.getRequestURI());

        if(Helpers.isPresentOnce(urlParams, "teamToken"))
        {
            token = urlParams.get("teamToken").get(0);
        }*/

        return token;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        try {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true"); //only debug!!!
            if(exchange.getRequestHeaders().containsKey("Access-Control-Request-Headers"))
            {
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", exchange.getRequestHeaders().get("Access-Control-Request-Headers").get(0));
            }

            if(exchange.getRequestMethod().equals("OPTIONS"))
            {
                exchange.sendResponseHeaders(200, 0);
            }



            Path p = Paths.get("webapp", exchange.getRequestURI().getPath());

            String loweredpath = exchange.getRequestURI().getPath().toLowerCase();

            File f = p.toFile();

            byte[] data = {};
            String contentType = "application/json";
            String postData = "";

            if(exchange.getRequestBody() != null)
            {
                try {
                    postData = new String(Helpers.readAllBytes(exchange.getRequestBody()), UTF_8);
                } catch (IOException ignored) {

                }
            }

            String teamToken = getTeamTokenValue(exchange);


            if (f.isFile() && f.canRead()) {
                data = Files.readAllBytes(p);
                contentType = Files.probeContentType(p);
            } else if (loweredpath.equals("/game")) {
                data = GameSerializer.JsonFromGame(game.getGameModel()).getBytes(UTF_8);
            } else if (loweredpath.equals("/map.jpg")) {
                synchronized (jpglock) {
                    data = currentjpg;
                }
                contentType = "image/jpeg";
            } else if (loweredpath.startsWith("/shipdetail")) {
                data = ShipDetail.getJson(exchange, game, teamToken);
            } else if (loweredpath.startsWith("/planaction") && exchange.getRequestMethod().equals("POST")) {
                data = PlanAction.doit(exchange, game, teamToken, postData);
            } else if (loweredpath.startsWith("/deleteactions") && exchange.getRequestMethod().equals("POST")) {
                data = DeleteActions.doit(exchange, game, teamToken, postData);
            }

            else if (loweredpath.equals("/login") && exchange.getRequestMethod().equals("POST")) {

                String teamId = Auth.getLogin(postData);

                String token = Auth.getSecretForTeam(teamId);

                data = String.format("{\"teamId\":\"%s\"}", teamId).getBytes(UTF_8);
                exchange.getResponseHeaders().add("Set-Cookie", "teamToken=" + token);

            } else if (loweredpath.equals("/log"))
            {
                data = CombatLog.getJson(exchange, game);
            }
            else {
                p = Paths.get("webapp", "index.html");
                data = Files.readAllBytes(p);
                contentType = Files.probeContentType(p);
            }

            if(contentType == null)
            {
                contentType = "text/plain";
            }

            if (contentType.equals("text/html") || contentType.equals("application/json")) {
                contentType += ";charset=utf-8";
            }

            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, data.length);

            outputStream.write(data);
        } catch (FriendlyException e)
        {
            byte[] err = e.getMessage().getBytes(UTF_8);
            exchange.sendResponseHeaders(400, err.length);
            outputStream.write(err);

        } catch (UnauthorizedException e)
        {
            byte[] err = e.getMessage().getBytes(UTF_8);
            exchange.sendResponseHeaders(401, err.length);
            outputStream.write(err);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            byte[] err = sw.toString().getBytes(UTF_8);

            exchange.sendResponseHeaders(500, err.length);
            outputStream.write(err);
        }
        outputStream.flush();
        outputStream.close();
    }


    /**
     * Exception that results in a 400 response with a friendly error message
     */
    public static class FriendlyException extends RuntimeException {

        public FriendlyException() {
        }

        public FriendlyException(String message) {
            super(message);
        }
    }


    /**
     * Exception that results in a 401 response with a friendly error message
     */
    public static class UnauthorizedException extends RuntimeException
    {

        public UnauthorizedException() {
        }

        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
