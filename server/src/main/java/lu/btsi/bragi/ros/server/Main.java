package lu.btsi.bragi.ros.server;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DatabaseModule());

        Server main = injector.getInstance(Server.class);
        main.start();

        System.out.println("listening on port " + main.getPort());
    }
}
