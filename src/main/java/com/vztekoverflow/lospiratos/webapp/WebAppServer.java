package com.vztekoverflow.lospiratos.webapp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.vztekoverflow.lospiratos.model.GameSerializer;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.RoundTimer;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import org.hildan.fxgson.FxGson;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WebAppServer implements HttpHandler {

    private HttpServer server = null;
    private Game game = null;

    public boolean isRunning() {
        return running;
    }

    private boolean running = false;
    PiratosWebSocket piratosocket = null;
    private RoundTimer timer = null;


    public void start() throws IOException {

        if (game == null) {
            throw new IllegalStateException("No game.");
        }
        if (running) {
            throw new IllegalStateException("Server already running.");
        }

        try {
            System.out.println("org token: " + Auth.getSecretForTeam("org"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/", this);
        server.setExecutor(Executors.newFixedThreadPool(10));

        server.start();

        piratosocket = new PiratosWebSocket(4242);
        piratosocket.start();



        game.addOnNextRoundStartedListener(roundNo -> {
            forceClientsRefresh();
            synchronized (TeamIsReady.readyTeams)
            {
                TeamIsReady.readyTeams.clear();
            }
        });

        running = true;



    }

    private byte[] currentjpg;
    private final Object jpglock = new Object();

    public void setCurrentMap(byte[] map) {
        synchronized (jpglock) {
            currentjpg = map;
        }
    }

    public void forceClientsRefresh()
    {
        piratosocket.broadcast("refresh");
    }

    public void setGame(Game g) {
        this.game = g;
    }

    public void setTimer(RoundTimer t)
    {
        timer = t;
    }

    public void stop(int delay) {
        if (!running) {
            throw new IllegalStateException("Server not running.");
        }

        server.stop(delay);
        try {
            piratosocket.stop(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        }*/

        /*if(exchange.getRequestURI() != null)
        {
            Map<String, List<String>> urlParams = Helpers.splitQuery(exchange.getRequestURI());

            if(Helpers.isPresentOnce(urlParams, "token"))
            {
                token = urlParams.get("token").get(0);
            }
        }*/


        return token;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        try {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true"); //only debug!!!
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            if(exchange.getRequestHeaders().containsKey("Access-Control-Request-Headers"))
            {
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", exchange.getRequestHeaders().get("Access-Control-Request-Headers").get(0));
            }

            if(exchange.getRequestMethod().equals("OPTIONS"))
            {
                exchange.sendResponseHeaders(204, -1);
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
                data = FxGson.create().toJson(new EnrichedGame(game)).getBytes(UTF_8);
            } else if (loweredpath.equals("/map.jpg")) {
                synchronized (jpglock) {
                    data = currentjpg;
                }
                contentType = "image/jpeg";
            } else if (loweredpath.startsWith("/shipdetail")) {
                data = ShipDetail.getJson(exchange, game, teamToken);
            } else if (loweredpath.startsWith("/getavailableenhancements")) {
                data = AvailableEnhancements.getJson(exchange, game, teamToken);
            } else if (loweredpath.startsWith("/planactionandcommit") && exchange.getRequestMethod().equals("POST")) {
                data = PlanAction.doit(exchange, game, teamToken, postData, true);
            } else if (loweredpath.startsWith("/planaction") && exchange.getRequestMethod().equals("POST")) {
                data = PlanAction.doit(exchange, game, teamToken, postData, false);
            } else if (loweredpath.startsWith("/getactioncost") && exchange.getRequestMethod().equals("POST")) {
                data = GetActionCost.doit(exchange, game, teamToken, postData, false);
            } else if (loweredpath.startsWith("/deleteactions") && exchange.getRequestMethod().equals("POST")) {
                data = DeleteActions.doit(exchange, game, teamToken, postData);
            } else if (loweredpath.startsWith("/createship") && exchange.getRequestMethod().equals("POST") )
            {
                data = CreateShip.doit(exchange, game, teamToken, postData);
            } else if (loweredpath.startsWith("/teamisready") && exchange.getRequestMethod().equals("GET") )
            {
                data = TeamIsReady.getJson(exchange, game, teamToken);
            } else if (loweredpath.startsWith("/teamisready") && exchange.getRequestMethod().equals("POST") )
            {
                data = TeamIsReady.set(exchange, game, teamToken, postData);
            } else if (loweredpath.startsWith("/roundend"))
            {
                if( timer != null)
                {
                    data = Long.toString(timer.getEndTimestamp()).getBytes(UTF_8);
                } else {
                    data = new byte[0];
                }

            } else if (loweredpath.startsWith("/org/restarttimer"))
            {
                Auth.assertAuthTeam("org", teamToken);
                timer.restartTimer();
            } else if (loweredpath.startsWith("/org/stoptimer"))
            {
                Auth.assertAuthTeam("org", teamToken);
                timer.stop();
            }else if (loweredpath.startsWith("/org/timerlength") && exchange.getRequestMethod().equals("GET"))
            {
                Auth.assertAuthTeam("org", teamToken);
                data = Integer.toString(timer.getNextRoundLength()).getBytes(UTF_8);
            }else if (loweredpath.startsWith("/org/timerlength") && exchange.getRequestMethod().equals("POST"))
            {
                Auth.assertAuthTeam("org", teamToken);
                timer.setNextRoundLength(Integer.parseInt(postData));
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
