package Configurations;

public class Configurations {

    protected static int SIZE_OF_GRID = 5;

    protected final static String PATH_TO_CREATE_SCRIPT = "src/main/resources/db/create.sql";

    protected final static String PATH_TO_INSERT_SCRIPT = "src/main/resources/db/insert.sql";

    protected final static String[] DATABASE_CREDIT = new String[]
            {
                    "--url", "jdbc:hsqldb:mem:db",
                    "--user", "sa", "--password", ""
            };

}
