package junggoNara;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PostDAO {

private static final String DRIVER
    = "oracle.jdbc.driver.OracleDriver";
private static final String URL
    = "jdbc:oracle:thin:@localhost:1521:XE";

private static final String USER = "scott"; //DB ID
private static final String PASS = "tiger"; //DB �н�����
Member_List mList;
	



public Connection getConn(){
    Connection con = null;
   
    try {
        Class.forName(DRIVER); //1. ����̹� �ε�
        con = DriverManager.getConnection(URL,USER,PASS); //2. ����̹� ����
       
    } catch (Exception e) {
        e.printStackTrace();
    }
   
    return con;
}

public static void main(String[] args) {
	Connection con = new PostDAO().getConn();
}

//���̺� �ҷ�����
public Vector getTableList()
{
	
	Vector data = new Vector();
	
    Connection con = null;      
    PreparedStatement ps = null; 
    ResultSet rs = null;        
	
    try{
        
        con = getConn();
        String sql = "select * from  Post order by post_id desc";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
       
        while(rs.next()){
        	int post_id = rs.getInt("post_id");
            String title = rs.getString("title");
            String id = rs.getString("id");
            String date = rs.getDate("time").toString();
            
           
            Vector row = new Vector();
            row.add(post_id);
            row.add(title);
            row.add(id);
            row.add(date);
            
           
            data.add(row);             
        }//while
    }catch(Exception e){
        e.printStackTrace();
    }finally
    {
  	  try {
			con.close();
			ps.close();
	          rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    return data;
}

public Vector getTableList(String college)
{
	
	Vector data = new Vector();
	
    Connection con = null;      
    PreparedStatement ps = null; 
    ResultSet rs = null;        
	
    try{
        
        con = getConn();
        String sql = "select * from  Post where college = '" + college + "' order by post_id desc";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
       
        while(rs.next()){
        	int post_id = rs.getInt("post_id");
            String title = rs.getString("title");
            String id = rs.getString("id");
            String date = rs.getDate("time").toString();
            
           
            Vector row = new Vector();
            row.add(post_id);
            row.add(title);
            row.add(id);
            row.add(date);
            
           
            data.add(row);             
        }//while
    }catch(Exception e){
        e.printStackTrace();
    }finally
    {
  	  try {
			con.close();
			ps.close();
	          rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    return data;
}
//Post ���� �Է¹ޱ�
public PostDTO getPostDTO(int Post_id){
	   
    PostDTO dto = new PostDTO();
   
    Connection con = null;       //����
    PreparedStatement ps = null; //���
    ResultSet rs = null;         //���
   
    try {
       
  	  con = getConn();
        String sql = "select * from Post where post_id=?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, Post_id);
       
        rs = ps.executeQuery();
       
        if(rs.next()){
        	dto.setId(rs.getString("id"));
        	dto.setPost_id(rs.getInt("post_id"));
            dto.setTitle(rs.getString("title"));
            dto.setContent(rs.getString("content"));
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    finally
    {
  	  try {
			con.close();
			ps.close();
	          rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
   
    return dto;    
}
public boolean updatePost(PostDTO vMem){
    boolean ok = false;
    Connection con = null;
    PreparedStatement ps = null;
    
    try{
       
        con = getConn();           
        String sql = "update post set title=?, content=? where post_id= ?";
       
        ps = con.prepareStatement(sql);
       
        ps.setString(1,vMem.getTitle());
        ps.setString(2,vMem.getContent());
        ps.setInt(3, vMem.getPost_id());
       
        int r = ps.executeUpdate(); //���� -> ����
        // 1~n: ���� , 0 : ����
       
        if(r>0) ok = true; //������ �����Ǹ� ok���� true�� ����
       
    }catch(Exception e){
        e.printStackTrace();
    }finally
    {
  	  try {
			con.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
   
    return ok;
}
public boolean insertPost(PostDTO dto){
	   
    boolean ok = false;
   
    Connection con = null;       //����
    PreparedStatement ps = null; //���
    ResultSet rs = null;
   
    try{
       
        con = getConn();
        
        String sql = "insert into Post(" +
    			"post_id,id,title,content,college) "+
    			"values(tmp_seq.NEXTVAL,?,?,?,?)";
        
        ps = con.prepareStatement(sql);
        ps.setString(1, dto.getId());
        ps.setString(2, dto.getTitle());
        ps.setString(3, dto.getContent());
        ps.setString(4, dto.getCollege());
        
        int r = ps.executeUpdate();      
        
        ok= true;
        
    }catch(Exception e){
        e.printStackTrace();
    }finally
    {
  	  try {
			con.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
   
    return ok;
}

public boolean deletePost(int post_id){
	   
    boolean ok =false ;
    Connection con =null;
    PreparedStatement ps =null;
   
    try {
        con = getConn();
        String sql = "delete from post_id where post_id = ?";
       
        ps = con.prepareStatement(sql);
        ps.setInt(1, post_id);
        int r = ps.executeUpdate(); // ���� -> ����
       
        if (r>0) ok=true; //������;
       
    } catch (Exception e) {
        System.out.println(e + "-> �����߻�");
    } finally
    {
  	  try {
			con.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }     
    return ok;
}



}