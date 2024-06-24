
package autocratetable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import text.DbUtil;

public class AutoCrateTable {
    
public static DbUtil db=new DbUtil();
static PreparedStatement ps;
static ResultSet rs;
static String sql="";
    public static void main(String[] args) {
        saveEmp("Mahabur","Mahabur@gmail.com" ,"1552478");
        saveEmp("Raju","Raju@gmail.com" ,"15855478");
        
        showEmp();
        deleteEmp(2);
        System.out.println("\n aftar delete");
        showEmp();
        updateEmp("kutub", "kutub@gmail.com", "5214545", 1);
        System.out.println("\n aftar update \n");
        showEmp();
    }
    public  static void saveEmp(String name,String emal,String cellno ){
    sql="insert into sanaullah(  name,emal,cellno) values(?,?,?)";
    try {
        ps=db.getCon().prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, emal);
                ps.setString(3, cellno);
               ps.executeUpdate();
               ps.close();
               db.getCon().close();
                } catch (SQLException ex) {
        Logger.getLogger(AutoCrateTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    
    public static void showEmp(){
    sql="select * from sanaullah";
    try {
        ps=db.getCon().prepareStatement(sql);
        rs=ps.executeQuery();
        while(rs.next()){
        String name=rs.getString("name");
        String emal=rs.getString("emal");
        String cellno=rs.getString("cellno");
        String empoyeeId=rs.getString("empoyeeId");
        
        
            System.out.println("Emp ID:"+ empoyeeId+"\tName:"+name+"\tEmal"+emal+"\tcellno"+cellno);
        
        }
        ps.close();
        rs.close();
        db.getCon().close();
    } catch (SQLException ex) {
        Logger.getLogger(AutoCrateTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    }
    public static  void deleteEmp(int empoyeeId ){
    sql="delete from sanaullah where empoyeeId=?";
    try {
        ps=db.getCon().prepareStatement(sql);
        ps.setInt(1, empoyeeId);
        ps.executeUpdate();
        ps.close();
        db.getCon().close();
        
    } catch (SQLException ex) {
        Logger.getLogger(AutoCrateTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    public static  void updateEmp(String name,String emal,String cellno,int empoyeeId){
     sql="update sanaullah set name=?,emal=?,cellno=? where empoyeeId=?";
    try {
        ps=db.getCon().prepareStatement(sql);
          ps.setString(1, name);
                ps.setString(2, emal);
                ps.setString(3, cellno);
                ps.setInt(4, empoyeeId);
                
               ps.executeUpdate();
               ps.close();
               db.getCon().close();
    } catch (SQLException ex) {
        Logger.getLogger(AutoCrateTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
}
