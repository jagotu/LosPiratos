package com.vztekoverflow.lospiratos.webapp;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.viewmodel.Game;

public class WebAppServer implements HttpHandler {

    private HttpServer server = null;
    private Game game = null;

    public boolean isRunning() {
        return running;
    }

    private boolean running = false;

    public WebAppServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/", this);
        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    public void start() {

        if (game == null) {
            throw new IllegalStateException("No game.");
        }
        if (running) {
            throw new IllegalStateException("Server already running.");
        }

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
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        try {


            Path p = Paths.get("webapp", exchange.getRequestURI().getPath());

            String loweredpath = exchange.getRequestURI().getPath().toLowerCase();

            File f = p.toFile();

            byte[] data = {};
            String contentType = "application/json";

            if (f.isFile() && f.canRead()) {
                data = Files.readAllBytes(p);
                contentType = Files.probeContentType(p);
            } else if (loweredpath.equals("/game")) {
                data = GameSerializer.JsonFromGame(game.getGameModel()).getBytes(StandardCharsets.UTF_8);
            } else if (loweredpath.equals("/map.jpg")) {
                synchronized (jpglock) {
                    data = currentjpg;
                }
                contentType = "image/jpeg";
            } else if (loweredpath.startsWith("/shipdetail")) {
                data = ShipDetail.getJson(exchange, game);
            } else {
                p = Paths.get("webapp", "index.html");
                data = Files.readAllBytes(p);
                contentType = Files.probeContentType(p);
            }

            if (contentType.equals("text/html") || contentType.equals("application/json")) {
                contentType += ";charset=utf-8";
            }

            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, data.length);

            outputStream.write(data);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            byte[] err = sw.toString().getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(500, err.length);
            outputStream.write(err);
        }
        outputStream.flush();
        outputStream.close();
    }

    public class FriendlyException extends Exception {

    }
}
