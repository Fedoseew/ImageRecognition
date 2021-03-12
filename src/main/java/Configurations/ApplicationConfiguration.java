package Configurations;

public class ApplicationConfiguration extends Configurations {

    public static int getSizeOfGrid() { return Configurations.SIZE_OF_GRID; }

    public static String[] getDatabaseCredits() { return Configurations.DATABASE_CREDIT; }

    public static String getPathToInsertScripts() { return  Configurations.PATH_TO_INSERT_SCRIPT; }

    public static String getPathToCreateScripts() { return Configurations.PATH_TO_CREATE_SCRIPT; }

    public synchronized static void setSizeOfGrid(int sizeOfGrid) { Configurations.SIZE_OF_GRID = sizeOfGrid; }
}
