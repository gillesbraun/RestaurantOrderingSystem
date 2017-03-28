package lu.btsi.bragi.ros.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Singleton
    @Provides
    public DSLContext getConnection() {
        Config config = ConfigFactory.parseFile(new File("ros.conf"));
        String userName = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        String db = config.getString("mysql.db");
        String url = "jdbc:mysql://localhost:3306/" + db + "?autoReconnect=true&useSSL=false";

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            return DSL.using(connection, SQLDialect.MYSQL);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not connect to mysql server. Errors should be above.");
            System.exit(2);
            return null;
        }
    }
}
