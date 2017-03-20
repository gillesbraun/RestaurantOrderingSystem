package lu.btsi.bragi.ros.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gillesbraun on 07/03/2017.
 */
public class WebServerImages {

    public WebServerImages(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("webserver started on port: "+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

    class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            Map<String, String> queryMap = queryToMap(t.getRequestURI().getQuery());
            File file = null;
            if(queryMap.get("category") != null) {
                file = new File(queryMap.get("category") + ".png");
                if(!file.exists()) {
                    try {
                        file = new File(getClass().getResource("/noimage.png").toURI());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                OutputStream os = t.getResponseBody();
                String response = "<h1>please specify a category.</h1>";
                t.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
                os.close();
                return;
            }

            OutputStream os = t.getResponseBody();

            if(t.getRequestMethod().equals("POST")) {
                handleImagePost(t, file);
            } else {
                t.sendResponseHeaders(200, file.length());
                Files.copy(file.toPath(), os);
            }

            os.close();
        }

        private void handleImagePost(HttpExchange t, File file) throws IOException {
            BufferedImage read = ImageIO.read(t.getRequestBody());
            ImageIO.write(read, "PNG", file);

            String respons = "OK";
            t.sendResponseHeaders(200, respons.getBytes().length);
            t.getResponseBody().write(respons.getBytes());
        }
    }

}
