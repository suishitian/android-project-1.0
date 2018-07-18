package MySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class mysqlJ {
	private 
		String username;
		String password;
		int port_sql;
		static Statement sql_s = null;
		static int database_flag=0;
		String database;
		String table;
	public static String getFriendListName(String account){
		return account+"_friend_list";
	}
		
	public mysqlJ(String name,String word,int port){
		username = name;
		password = word;
		port_sql=port;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:"+port_sql+"/";
		
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			if(!conn.isClosed()){
				System.out.println("Succeeded connecting to the Database!");
			}
			sql_s = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public mysqlJ(){
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/";
		username = "root";
		password = "a4533776";
		port_sql = 3306;
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			if(!conn.isClosed()){
				System.out.println("Succeeded connecting to the Database!");
			}
			sql_s = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public void setDatabase(String database){
		String statement = "USE "+database+";";
		try{
			sql_s.executeQuery(statement);
			database_flag=1;
			this.database=database;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public void showDatabase(){
		String statement = "SHOW DATABASES;";
		try{
			ResultSet res = sql_s.executeQuery(statement);
			while(res.next()){
				System.out.println(res.getString("database"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public void showTable(){
		if(database_flag==0){
			System.out.println("must set database first");
			return;
		}
		String statement = "SHOW TABLES;";
		try{
			ResultSet res = sql_s.executeQuery(statement);
			while(res.next()){
				System.out.println(res.getString("Tables_in_"+database));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	//public String selectIP(String table , String name){
	//}
	public String getPassword(String table,String name){
		if(database_flag==0){
			System.out.println("must set database first");
			return "";
		}
		String statement = "SELECT password"+" FROM "+table+" WHERE name="+"'"+name+"';";
		//System.out.println(statement);
		try{
			ResultSet ans = sql_s.executeQuery(statement);
			ans.next();
			String res =ans.getString("password"); 
			return res;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
		finally{}
	}
	public String getUserID(String table,String name){
		if(database_flag==0){
			System.out.println("must set database first");
			return "";
		}
		String statement = "SELECT id"+" FROM "+table+" WHERE name="+"'"+name+"';";
		//System.out.println(statement);
		try{
			ResultSet ans = sql_s.executeQuery(statement);
			ans.next();
			String res =ans.getString("id"); 
			return res;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
		finally{}
	}
	public static void createTable(String name,String[] col_name,String[] col_type,String[] col_null,String primary){
		if(database_flag==0){
			System.out.println("must set database first");
			return;
		}
		String statement = "CREATE TABLE "+name+"(";
		for(int i=0;i<col_name.length;i++){
			statement += col_name[i]+" "+col_type[i]+" "+col_null[i]+", ";
		}
		statement += "PRIMARY KEY ("+primary+"))ENGINE=InnoDB;";
		System.out.println(statement);
		try {
			sql_s.executeUpdate(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public String insertNewAccount(String table ,String name ,String password){
		if(database_flag==0){
			System.out.println("must set database first");
			return "";
		}
		String statement = "INSERT INTO "+table+" VALUES(NULL,'"+name+"','"+password+"',0,0,0);";
		String statement2 = "SELECT id"+" FROM "+table+" WHERE name="+"'"+name+"';";
		//System.out.println(statement);
		try{
			sql_s.executeUpdate(statement);
			ResultSet ans = sql_s.executeQuery(statement2);
			ans.next();
			String res = ans.getString("id");
			return res;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
		finally{}
	}
	public static void insertData(String table,String[] content,String col_type[]){
		System.out.println("insertData");
		if(database_flag==0){
			System.out.println("must set database first");
			return;
		}
		String statement = "INSERT INTO "+table+" VALUES(";
		for(int i=0;i<content.length;i++){
			if(col_type[i].equals("int")){
				statement += content[i];	
			}else if(col_type[i].equals("char")){
				statement += "'"+content[i]+"'";
			}
			
			if(i!=content.length-1){
				statement+=" ,";
			}
		}
		statement += ");";
		try {
			sql_s.executeUpdate(statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getSingleData(String table,String col_name,String select,String select_content,String content_type){
		if(database_flag==0){
			System.out.println("must set database first");
			return "";
		}
		String statement = "SELECT "+col_name+" FROM "+table+" WHERE "+select+" = ";
		if(content_type.equals("int")){
			statement += select_content+";";
		}else if(content_type.equals("char")){
			statement += "'"+select_content+"';";
		}
		
		try{
			//sql_s.executeUpdate(statement);
			ResultSet ans = sql_s.executeQuery(statement);
			ans.next();
			String res = ans.getString(col_name);
			return res;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
		finally{}
	}
	
	public static void updateSingleData(String table,String col_name,String set_content,String where,String where_content,String[] content_type){
		System.out.println("updateSingleData");
		if(database_flag==0){
			System.out.println("must set database first");
			return;
		}
		String statement = "UPDATE "+table+" SET "+col_name+"=";
		
		if(content_type[0].equals("int")){
			statement += set_content;
		}else if(content_type[0].equals("char")){
			statement += "'"+set_content+"'";
		}
		statement+= " WHERE "+where+"=";
		if(content_type[1].equals("int")){
			statement += where_content;
		}else if(content_type[1].equals("char")){
			statement += "'"+where_content+"'";
		}
		statement+=";";
		System.out.println(statement);
		try{
			//sql_s.executeUpdate(statement);
			sql_s.executeUpdate(statement);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public static List<String> getColDataList(String table,String col_name){
		if(database_flag==0){
			System.out.println("must set database first");
			return null;
		}
		List<String> list = new ArrayList<>();
		String statement = "SELECT "+col_name+" FROM "+table+";";

		try{
			//sql_s.executeUpdate(statement);
			ResultSet ans = sql_s.executeQuery(statement);
			while(ans.next()){
				String res = ans.getString(col_name);
				list.add(res);
			}
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{}
	}
	
	public static HashSet<String> getColDataSet(String table,String col_name){
		System.out.println("getColDataSet");
		if(database_flag==0){
			System.out.println("must set database first");
			return null;
		}
		HashSet<String> list = new HashSet<>();
		String statement = "SELECT "+col_name+" FROM "+table+";";
		try{
			//sql_s.executeUpdate(statement);
			ResultSet ans = sql_s.executeQuery(statement);
			while(ans.next()){
				String res = ans.getString(col_name);
				list.add(res);
			}
			return list;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{}
	}
	public static void createFriendsList(String account){
		//public void createTable(String name,String[] col_name,
		//   String[] col_type,String[] col_null,String primary){
		
		createTable(mysqlJ.getFriendListName(account),new String[]{"friend_account"},new String[]{"char(50)"},
				new String[]{"NOT NULL"},"friend_account");
		System.out.println("createFriendList");
	}
}