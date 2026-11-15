package com.shop.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        String createLogTable = """
            CREATE TABLE IF NOT EXISTS log_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                operation TEXT,
                table_name TEXT,
                row_id INTEGER,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;        
        
        //Now create triggers for each table
        String createAirlineInsertTriggers = """
                
            CREATE TRIGGER IF NOT EXISTS log_insert_airline
            AFTER INSERT ON airline
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('INSERT', 'airline', NEW.id);
            END;        
        """;

        String createAirlineUpdateTriggers="""
            CREATE TRIGGER IF NOT EXISTS log_update_airline
            AFTER UPDATE ON airline
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('UPDATE', 'airline', NEW.id);
            END;
            """;

        String createAirlineDeleteTriggers="""
            CREATE TRIGGER IF NOT EXISTS log_delete_airline
            AFTER DELETE ON airline
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('DELETE', 'airline', OLD.id);
            END;
        """;

        
        String createFlightInsertTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_insert_flight
            AFTER INSERT ON flight
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('INSERT', 'flight', NEW.id);
            END;
            """;

        String createFlightUpdateTriggers ="""
            CREATE TRIGGER IF NOT EXISTS log_update_flight
            AFTER UPDATE ON flight
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('UPDATE', 'flight', NEW.id);
            END;
        """;

        String createFlightDeleteTriggers ="""
            CREATE TRIGGER IF NOT EXISTS log_delete_flight
            AFTER DELETE ON flight
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('DELETE', 'flight', OLD.id);
            END;
        """;

        String createTicketInsertTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_insert_ticket
            AFTER INSERT ON ticket
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('INSERT', 'ticket', NEW.id);
            END;
        """;

        String createTicketUpdateTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_update_ticket
            AFTER UPDATE ON ticket
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('UPDATE', 'ticket', NEW.id);
            END;
        """;

        String createTicketDeleteTriggers ="""
            CREATE TRIGGER IF NOT EXISTS log_delete_ticket
            AFTER DELETE ON ticket
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('DELETE', 'ticket', OLD.id);
            END;
        """;

        String createCityInsertTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_insert_city
            AFTER INSERT ON city
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('INSERT', 'city', NEW.id);
            END;                   
        """;

        String createCityUpdateTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_update_city
            AFTER UPDATE ON city
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('UPDATE', 'city', NEW.id);
            END;
        """;

        String createCityDeleteTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_delete_city
            AFTER DELETE ON city
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('DELETE', 'city', OLD.id);
            END;
        """;

        String createPassengerInsertTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_insert_passenger
            AFTER INSERT ON passenger
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('INSERT', 'passenger', NEW.id);
            END;           
        """;

        String createPassengerUpdateTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_update_passenger
            AFTER UPDATE ON passenger
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('UPDATE', 'passenger', NEW.id);
            END;
        """;

        String createPassengerDeleteTriggers = """
            CREATE TRIGGER IF NOT EXISTS log_delete_passenger
            AFTER DELETE ON passenger
            BEGIN
                INSERT INTO log_table (operation, table_name, row_id)
                VALUES ('DELETE', 'passenger', OLD.id);
            END;
        """;

        // Execute SQL statements to create triggers
        jdbcTemplate.execute(createLogTable);
        jdbcTemplate.execute(createAirlineInsertTriggers);
        jdbcTemplate.execute(createAirlineUpdateTriggers);
        jdbcTemplate.execute(createAirlineDeleteTriggers);
        jdbcTemplate.execute(createFlightInsertTriggers);
        jdbcTemplate.execute(createFlightUpdateTriggers);
        jdbcTemplate.execute(createFlightDeleteTriggers);
        jdbcTemplate.execute(createTicketInsertTriggers);
        jdbcTemplate.execute(createTicketUpdateTriggers);
        jdbcTemplate.execute(createTicketDeleteTriggers);
        jdbcTemplate.execute(createCityInsertTriggers);
        jdbcTemplate.execute(createCityUpdateTriggers);
        jdbcTemplate.execute(createCityDeleteTriggers);
        jdbcTemplate.execute(createPassengerUpdateTriggers);
        jdbcTemplate.execute(createPassengerInsertTriggers);
        jdbcTemplate.execute(createPassengerDeleteTriggers);
    }
}

