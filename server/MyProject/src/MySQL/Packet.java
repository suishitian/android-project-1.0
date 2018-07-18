package MySQL;

public class Packet {
	private String content;
	private String[] str;
	private int length;
	
	public Packet(String content){
		System.out.println("create Packet");
		this.content = content;
		str = content.split(":");
		length = str.length;
	}
	public int getSize(){
		return length;
	}
	public String get(int i){
		if(i<length&&i>=0){
			return str[i];
		}else{
			return "";
		}
	}
}