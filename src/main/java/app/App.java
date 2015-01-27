package app;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class App {

    public static void main(String[] args) throws Exception {
        URI uri = URI.create("http://localhost:8888/");
        ResourceConfig rc = new ResourceConfig();
        rc.register(MemberResource.class);
        HttpServer server = JdkHttpServerFactory.createHttpServer(uri, rc);

        System.out.println("started.");
        System.in.read();
        server.stop(3);
    }

}
