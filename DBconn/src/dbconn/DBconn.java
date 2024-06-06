
package dbconn;



import dbutile.DbUtile;
import dbutile.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBconn {
static PreparedStatement ps;
static ResultSet rs;
static DbUtile db=new DbUtile();
  
    public static void main(String[] args) {
        
        Student student=new Student();
        List<Student> studentList=getById(1);
         
           student.setName(studentList.get(0).getName());
        student.setEmail("Rajujee59@gmail.com");
        student.setAddress(studentList.get(0).getAddress());
        student.setCell(studentList.get(0).getCell());
        student.setId(studentList.get(0).getId());

        editData(student);
        showData();
        
        
        
    }
    
    public  static  void  saveData(){
    
    String insertSql="insert into jeejava(name,email,address,cell)"+"values(?,?,?,?)";
    
    try {
        ps=db.getCon().prepareStatement(insertSql);
        
        ps.setString(1, "sanaullah");
            ps.setString(2, "sanaullah@gmail.com");
            ps.setString(3, "Chapainewbgonj");
            ps.setString(4, "01741779995");

            ps.executeUpdate();
            ps.close();
            db.getCon().close();
 
        
    } catch (SQLException ex) {
        Logger.getLogger(DBconn.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    
    }
    public  static void showData(){
    
    String selectSql="select* from jeejava";
    try {
        ps=db.getCon().prepareStatement(selectSql);
        
        rs=ps.executeQuery();
        
        while(rs.next()){
        
                rs=ps.executeQuery();
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String email=rs.getString("email");
                String address=rs.getString("address");
                String cell=rs.getString("cell");

            System.out.println("id"+id+"name"+name+"email"+email+"address"+address+"cell"+cell);
            
        
        
        }
        
         ps.close();
            rs.close();
            db.getCon().close();
        
        
        
    } catch (SQLException ex) {
        Logger.getLogger(DBconn.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    
     public  static void editeData(Student s){
     
     String sql = "update jeejava set name=?, email=?, address=?, cell=? where id=?";
     
    try {
        ps=db.getCon().prepareStatement(sql);
        
       
        
        
        ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getAddress());
            ps.setString(4, s.getCell());
            ps.setInt(5, s.getId());
        ps.executeUpdate();
        db.getCon().close();
        System.out.println("Edite Successful");
        
        
        
    } catch (SQLException ex) {
        Logger.getLogger(DBconn.class.getName()).log(Level.SEVERE, null, ex);
    }
     }
      public static List<Student> getById(int id) {
        List<Student> studentList = new ArrayList<>();

        String sql = "select * from student where id=?";
     
    try {
        ps=db.getCon().prepareStatement(sql);
        ps.setInt(1, id);
        rs=ps.executeQuery();
        
        
        while(rs.next(){
            
            
        }
    }
    } catch (SQLException ex) {
        Logger.getLogger(DBconn.class.getName()).log(Level.SEVERE, null, ex);
    }
    return studentList;
}
