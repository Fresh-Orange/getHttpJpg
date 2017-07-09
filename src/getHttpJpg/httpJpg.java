package getHttpJpg;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class httpJpg {
	static int fileCode = 0;
	static int ranFileCode = 0;
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in); 
		String iurl = sc.nextLine();
		//downLoadAllLink(iurl);
		//downLoadAllLink("http://tieba.baidu.com/p/3910046714/");
		downloadIMG(iurl);
		//checkType("http://tse1.mm.bing.net/th?id=OET.9d56d079628e4038aeb38fc70e178add&w=272&h=135&c=7&rs=1&qlt=90&o=4&pid=1.9");
	}
	private static void downLoadAllLink(String strUrl) throws IOException {
		BufferedInputStream bis = null;
		HttpURLConnection urlconnection = null;
		URL url = null;
		url=new URL(strUrl);
		urlconnection=(HttpURLConnection)url.openConnection();
		urlconnection.connect();
		bis=new BufferedInputStream(urlconnection.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line = null;
		String whole = "";
		while((line = br.readLine()) != null){
			//System.out.println("line: " + line);
			whole = whole + line;
		}
		
		//String regex = "href=[\"'](http://www[\\S]*)[\"']"; 2017.7.9之前
		String regex = "(href|src)=[\"'](http://[\\S]*)[\"']";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(whole);
		while(m.find())
		{
			System.out.println(m.group(2));
			downloadIMG(m.group(2));
		}
		
	}
	public static String checkType(String strUrl) throws IOException {
		BufferedInputStream bis = null;
		HttpURLConnection urlconnection = null;
		URL url = null;
		url=new URL(strUrl);
		urlconnection=(HttpURLConnection)url.openConnection();
		urlconnection.connect();
		bis=new BufferedInputStream(urlconnection.getInputStream());
		String type = HttpURLConnection.guessContentTypeFromStream(bis);
		System.out.println("file type:"+ type);
		return type;
	}
	
	public static void downloadIMG(String strUrl) throws IOException {
		Random random = new Random();
		ranFileCode = random.nextInt(1000);
		
		BufferedInputStream bis = null;
		HttpURLConnection urlconnection = null;
		URL url = null;
		try{
			url=new URL(strUrl);
			urlconnection=(HttpURLConnection)url.openConnection();
			urlconnection.connect();
			bis=new BufferedInputStream(urlconnection.getInputStream());
		}catch (IOException e) {
			System.out.println("RETURN!!");
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line = null;
		String whole = "";
		while((line = br.readLine()) != null){
			whole = whole + line;
		}
		Vector<String> vs = regIMG(whole);
		if (!vs.isEmpty())
		{
			for (String s:vs)
			{
				downToPath(s,"G:\\emoj\\");
			}
		}
	}
	
	private static void downToPath(String urlPath,String savePath) throws IOException{
		BufferedInputStream bis = null;
		HttpURLConnection urlconnection = null;
		URL url = null;
		System.out.println(urlPath);
		if (urlPath.indexOf("http://")<0)
			return;
		try {
			url=new URL(urlPath);
			urlconnection=(HttpURLConnection)url.openConnection();
		
			urlconnection.connect();
			bis=new BufferedInputStream(urlconnection.getInputStream());
		} catch (IOException e) {
			System.out.println("RETURN!!");
			return;
		}
		
		//byte[] buf = new byte[1024*1024];
		int b = 0;
		String regex = "(jpg|gif|png|jpeg)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(urlPath);
		String type = "jpg";
		if (m.find())
			type = m.group();
		//System.out.println(urlPath);
		FileOutputStream fos = new FileOutputStream(savePath+Integer.toString(ranFileCode)+Integer.toString(fileCode)+"."+type);
		fileCode++;
		System.out.println("OK to == " +savePath+Integer.toString(ranFileCode)+Integer.toString(fileCode)+"."+type);
		while((b = bis.read())>=0)
		{
			fos.write(b);
		}
		fos.flush();
		fos.close();
		
	}
	private static Vector<String> regIMG(String httpCode) {
		
		String regex = "(data-original|src)=[\"']([\\S]*(jpg|gif|png|jpeg))[\"']";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(httpCode);
		Vector<String> vs = new Vector<String>();
		while(m.find())
		{
			//System.out.println("reg");
			String urlPath = m.group(2);
			if (urlPath.indexOf("http") < 0)
				urlPath = "http:"+urlPath;
			//System.out.println(urlPath);
			vs.addElement(urlPath);
		}
		return vs;
	}
}

