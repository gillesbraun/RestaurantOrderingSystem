package lu.btsi.bragi.ros.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by gillesbraun on 14/02/2017.
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    public DSLContext getConnection() {
        String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/ros";

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            return DSL.using(connection, SQLDialect.MYSQL);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
