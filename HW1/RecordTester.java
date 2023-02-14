import java.sql.*;
import java.util.Random;

public class RecordTester{
  private static final String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/";
  private static final String SQLITE_DB_URL = "jdbc:sqlite:./";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "MySQLAdmin";

  public String dbms;
  public String db;
  public String table_name;
  public String type;
  public int num_rows;
  public int num_columns;
    
  public static void main(String[] args) {
    RecordTester rt = new RecordTester(args);
  }

  public RecordTester(String[] args){
      dbms = args[0];
      db = args[1];
      table_name = args[2];
      type = args[3];
      num_rows = Integer.parseInt(args[4]);
      num_columns = Integer.parseInt(args[5]);
      StringBuilder addRowSB = new StringBuilder();
      StringBuilder tableSB = new StringBuilder();

      try{
        Connection connection = null;

        if(dbms.equals("sqlite")){
          connection = connectToDB(0);
        }else if(dbms.equals("mysql")){
          connection = connectToDB(1);
        }else{
          System.out.println("Must be using mysql or sqlite");
        }

        //load JDBC driver
        if(type.equals("sqlite")){
          Class.forName("org.sqlite.JDBC");
        }
        else if(type.equals("mysql")){
          Class.forName("com.mysql.jdbc.Driver");
        }
        System.out.println("loaded JDBC driver");

        //sql statement used for all sql commands
        Statement statement = connection.createStatement();
        System.out.println("created statement");

        //drop table if already exists
        String dropTable = "DROP TABLE IF EXISTS " + table_name + ";";
        statement.executeUpdate(dropTable);
        System.out.println("dropped table if exist");
        
        //create table
        System.out.println("attempting creating table");
        for(int i=1; i<num_columns; i++){
          tableSB.append(",`" + i + "` " + type + " NOT NULL");
        }
        tableSB.append(");");
        String createTable = "CREATE TABLE " + table_name + "(`0` " + type + " NOT NULL " + tableSB.toString();
        //System.out.println(createTable);
        statement.executeUpdate(createTable);
        System.out.println("created table");

        //if index then set to have index related to first col (0)
        if(args[6].equals("TRUE")){
          String addIndex = "ALTER TABLE " + table_name +" ADD UNIQUE INDEX index_name 0" + ";";
          statement.executeUpdate(addIndex);
        }
        System.out.println("added index");
        
        //add rows, randomly generated vals using helper function
        String addRow = ""; 
        String temp = "";
        System.out.println("attempting inserting");
        for(int i=0; i<num_rows; i++){
          addRow = "INSERT INTO "+ table_name + " VALUES (" + randomRun(type);
          //System.out.println(addRow);
          addRowSB.append(addRow);
          for(int j=1; j<num_columns; j++){
            temp = ", " + randomRun(type);
            addRowSB.append(temp);
          }
          addRowSB.append(");");
          addRow=addRowSB.toString();
          //System.out.println(addRow);
          statement.executeUpdate(addRow);
        }
        System.out.println("insert complete");
        } 
        catch(Exception e){
          System.out.println("Exception " + e);
        }
  }

  private Connection connectToDB(int dbType){
    Connection connection = null;
    //sqlite
    if(dbType==0){
      try{
        connection = DriverManager.getConnection(SQLITE_DB_URL + db);
        System.out.println("Connected to SQLite DB");
      }
      catch(SQLException e){
        System.out.println(e);
      }
    }
    //mysql
    else if(dbType==1){
      try{
        connection = DriverManager.getConnection(MYSQL_DB_URL, USERNAME, PASSWORD);
        System.out.println("Connected to SQLite DB");

        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP DATABASE IF EXISTS homework1");

        statement.executeUpdate("CREATE DATABASE homework1");
        System.out.println("Database created successfully");

        statement.executeUpdate("USE homework1");
        System.out.println("Selected database successfully");
      }
      catch(SQLException e){
        System.out.println(e);
      }
    }
    else{
      System.out.println("Error connection to DB, must be MySQL or SQLite");
    }
    return connection;
  }

  private String randomRun(String type){
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    if(type.charAt(0)=='T'){
      int len = random.nextInt(100) + 1;
      for (int i = 0; i < len; i++) {
        sb.append(random.nextInt(2));
      }
    }
    else{
      for(int i=0; i<99; i++){
        sb.append(random.nextInt(2));
      }
    }
    //System.out.println(sb.toString());
    return sb.toString();
    }
}
