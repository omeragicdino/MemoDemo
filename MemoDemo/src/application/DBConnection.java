package application;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {
	
	 String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=memoDemoDB;user=dino; password=12345";
	 
	public ResultSet getRS(String SQL)throws Exception {	 
			Connection con = DriverManager.getConnection(connectionUrl); 
     		Statement stmt = con.createStatement(); 
         	ResultSet rs = stmt.executeQuery(SQL); // e.g. SQL = "SELECT * FROM \"User\"";
    		return rs;  	 
	}
        
	public void insertQuery(String SQL)throws Exception {	 
	     	Connection con = DriverManager.getConnection(connectionUrl); 
	     	Statement stmt = con.createStatement(); 
	        stmt.execute(SQL); //  e.g. SQL = "INSERT INTO User (Name,Password,STAT_CD) VALUES ('Dino','',5)";	
		}
		 
}
