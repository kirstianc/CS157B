import java.sql.*;
import java.util.Random;

public class RecordTester{
    public static void main(String[] args) {
      
      RecordTester rt = new RecordTester();
      String dbms = args[0];
      String db = args[1];
      String table_name = args[2];
      String type = args[3];
      int num_rows = Integer.parseInt(args[4]);
      int num_columns = Integer.parseInt(args[5]);
      String index = "";
      StringBuilder addRowSB = new StringBuilder();

      try{
        //load JDBC driver
        Class.forName("org.sqlite.JDBC");
        
        //db connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:dbms.db");
        
        //sql statement used for all sql commands
        Statement statement = connection.createStatement();
        
        //drop table if already exists
        String dropTable = "DROP TABLE IF EXISTS " + table_name;
        statement.executeUpdate(dropTable);

        //create table
        String createTable = "CREATE TABLE " + table_name;
        statement.executeUpdate(createTable);

        //add cols, named by i
        String addCol = ""; 
        for(int i=0; i<num_columns; i++){
          addCol = "ALTER TABLE "+ i + " " + type;
          statement.executeUpdate(addCol);
        }

        //if index then set to have index related to first col (0)
        if(args[6].equals("TRUE")){
          String addIndex = "ALTER TABLE " + table_name +" ADD UNIQUE INDEX index_name 0";
          statement.executeUpdate(addIndex);
        }

        //add rows, randomly generated vals using helper function
        String addRow = ""; 
        String temp = "";
        for(int i=0; i<num_rows; i++){
          addRow = "INSERT INTO "+ table_name + " " + i;
          addRowSB.append(addRow);
          for(int j=0; j<num_columns; j++){
            temp = " " + rt.randomRun(type);
            addRowSB.append(temp);
          }
          addRow=addRowSB.toString();
          statement.executeUpdate(addRow);
        }

        

        } catch(Exception e){

        }        
    }

    private String randomRun(String type){
      StringBuilder sb = new StringBuilder();
      Random random = new Random();
    
      if(type.charAt(0)=='T'){
        int len = random.nextInt(100) + 1;
        for (int i = 0; i < len; i++) {
          sb.append(random.nextInt(2));
        }
      }else{
        for(int i=0; i<99; i++){
          sb.append(random.nextInt(2));
        }
      }
      return sb.toString();
    }
}
