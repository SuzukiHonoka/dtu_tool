package com.nj.dtu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OpSqliteDB {
    
    private static final String Class_Name = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:dtuLog.db";
    
    public static void main(String args[]) {
        // load the sqlite-JDBC driver using the current class loader
        Connection connection = null;
        try {
            connection = createConnection();
            
            
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
          
            statement.executeUpdate("create table if not exists login (id integer, times integer, time string)");
          
        
            ResultSet rs = statement.executeQuery("select * from login");
            if(rs.next()){
            	  System.out.println(rs.getInt("times")+":"+rs.getString("time"));
            	  statement.executeUpdate("update login set times= 3 where id = 1");
            }else{
            	  statement.executeUpdate("insert into login values(1,0,'2020000')");
            }
            
            System.out.println("Success!");
        }  catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
    public static void init (){
        Connection connection = null;
        try {
            connection = createConnection();
            
            
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
          
            statement.executeUpdate("create table if not exists login (id integer, times integer, time long)");
          
        
            ResultSet rs = statement.executeQuery("select * from login");
            if(rs.next()){
//            	  System.out.println(rs.getInt("times")+":"+rs.getLong("time"));
//            	  statement.executeUpdate("update login set times= 3 where id = 1");
            }else{
            	  statement.executeUpdate("insert into login values(1,0,'2020000')");
            }
            
        }  catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
    public static boolean canContinue(){
        Connection connection = null;
        try {
            connection = createConnection();
            
            
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
          
        
            ResultSet rs = statement.executeQuery("select * from login");
            if(rs.next()){
            	  int time = rs.getInt("times");
            	  long now = System.currentTimeMillis();
            	  long old = rs.getLong("time");
            	  System.out.println(rs.getInt("times")+":"+rs.getLong("time")+":"+now);
            	  if(time>=3){
            		  if(now - old >5*60*1000){
            			  statement.executeUpdate("update login set times= 0 where id = 1");
            			  return true;
            		  }else{
            			  return false;
            		  }
            	  }
            	  
         
            }
           
        }  catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return true;
    }
    public static void addError(){
    	 Connection connection = null;
         try {
             connection = createConnection();
             
             
             Statement statement = connection.createStatement();
             statement.setQueryTimeout(30);  // set timeout to 30 sec.
           
         
             ResultSet rs = statement.executeQuery("select * from login");
             if(rs.next()){
             	  int time = rs.getInt("times");
             	  long now = System.currentTimeMillis();           	
                  time++;
             	 statement.executeUpdate("update login set times= "+time+",time ="+now+" where id = 1"); 
          
             }
             
         }  catch (SQLException e) {
             System.err.println("error:"+e.getMessage());
         } catch(Exception e) {
             e.printStackTrace();
         } finally{
             try {
                 if (connection != null)
                     connection.close();
             } catch (SQLException e) {
                 // connection close failed.
                 System.err.println(e);
             }
         }
    }
    
    // 创建Sqlite数据库连接
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(Class_Name);
        return DriverManager.getConnection(DB_URL);
    }

}