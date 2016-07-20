package common;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将mybatis generator生成的domain对象进行批量添加Column的处理，并自动继承BaseDomain，删掉id的get,set
 */
public class DomainObjectReplacementTool {

	static String path = "C:\\testspace\\mybatis-genertor";//源目录 根据实际需要更改
	static String newPath = "C:\\testspace\\mybatis-genertor\\new\\";//目标目录 根据实际需要更改

	public static void main(String[] args) throws IOException {
		
		File file = new File(path);
		
		if(file.isDirectory()){
			
			File[] fs = file.listFiles();
			
			for(File f : fs){
				
				if(f.isFile()){
					
					rewrite(f);
				}
			}
		}
		
		System.out.println("执行成功");
		
	}
	
	public static void rewrite(File originFile) throws IOException{
		
		if(originFile.exists()){
			
			Pattern p = Pattern.compile("private (\\w+) (\\w+);");
			
			Matcher m = null;
			
			
			BufferedReader br = new BufferedReader(new FileReader(originFile));
			
			FileWriter fw = new FileWriter(newPath + originFile.getName());
			
			StringBuffer sb = new StringBuffer();
			
			String content = "";
			
			while((content = br.readLine()) != null){
				content = content.replaceAll("(public class \\w*)", "$1 extends BaseDomain");
				
				content = content.replace("@Id", "");
				
				content = content.replaceAll("@GeneratedValue\\(strategy = GenerationType\\.IDENTITY\\)", "");
				
				m = p.matcher(content);
				
				if(m.find()){
					if(!m.group(2).equalsIgnoreCase("id")){
						
						content = content.replaceAll("private (\\w+) (\\w+);", "@Column(name = \"$2\")\n    private $1 $2;");
					}else{
						content = content.replace("private Long id;", "");
					}
				}
				
				//fw.append(content+"\n");
				sb.append(content+"\n");
			}
			
			String s = sb.toString();
			
			s = s.replaceAll( "\\s*\\/\\*\\*\\n"+
				      "\\s*\\*\\s\u4E3B\u952E\\n"+
				      "\\s*\\*/\\n", "");
			
			s = s.replaceAll( "/\\*\\*\\n"+
				      "\\s*\\*\\s.*\\n"+
				      "\\s*\\*\\n"+
				      "\\s*\\*\\s@return id .*\\n"+
				      "\\s*\\*/\\n"+
				      "\\s*public Long getId\\(\\)\\s\\{\\n"+
				      "\\s*return id;\\n"+
				      "\\s*\\}\\n", "");
			
			s = s.replaceAll( "/\\*\\*\\n"+
				      "\\s*\\*\\s.*\\n"+
				      "\\s*\\*\\n"+
				      "\\s*\\*\\s@param id.*\\n"+
				      "\\s*\\*/\\n"+
				      "\\s*public void setId.*\\n"+
				      "\\s*this.*\\n"+
				      "\\s*}\\n", "");
			
			fw.append(s);
			fw.flush();
			
			br.close();
			fw.close();
		}
		
	}
}
