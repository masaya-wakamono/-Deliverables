package othello;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class save {
	public static void main(String[] args){
		
//    	mysql --user=root --password
//		USE mysql;
//		SELECT * FROM othello;
		
		
		//DB�ڑ�����ݒ肷��
        String path = "jdbc:mysql://localhost:3306/mysql";  //�ڑ��p�X
        String id = "root";    //���O�C��ID
        String pw = "root";  //���O�C���p�X���[�h
      //SQL�����`����
        String sql = "INSERT INTO biosix VALUES(?, ?)";
        
        if(true) {
        	try(Connection conn = DriverManager.getConnection(path, id, pw)){

                conn.setAutoCommit(false);
                
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    ps.setString(1,"1");
                    ps.setString(2,"tanaka");
                    ps.executeUpdate();
                    conn.commit();
                    
                } catch (Exception e) {
                    conn.rollback();
                    System.out.println("�ꎞ�ۑ��Ɏ��s���܂���!");
                    throw e;
                }
                System.out.println("�ꎞ�ۑ��ɐ������܂���!");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("�ꎞ�ۑ��Ɏ��s���܂���!");
            }finally {
                System.out.println("�I��!");
            }
        }
//        try(Connection conn = DriverManager.getConnection(path, id, pw)){
//
//            conn.setAutoCommit(false);
//            
//            try(PreparedStatement ps = conn.prepareStatement(sql)){
//                ps.setString(1,"1");
//                ps.setString(2,"oohara");
//                ps.executeUpdate();
//                conn.commit();
//                
//            } catch (Exception e) {
//                conn.rollback();
//                System.out.println("rollback");
//                throw e;
//            }
//            System.out.println("�������������܂���!�Z�b�N�X!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("���s!�t�@�b�N!");
//        }finally {
//            System.out.println("�I��!");
//        }
	}
}
