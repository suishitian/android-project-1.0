package MySQL;

public class test {
	public static void main(String args[]){
		mysqlJ mysql = new mysqlJ("root","Sui4533776",3306);
		mysql.showDatabase();
		mysql.setDatabase("test");
		
		System.out.println(mysql.getSingleData("test1","col1","col2","a","char"));
		
		MultiThread mainProcess = new MultiThread(8889);
		mainProcess.receThreadRun();
	}
}