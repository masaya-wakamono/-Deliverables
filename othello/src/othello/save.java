package othello;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class save {
	public static void main(String[] args){
		
//    	mysql --user=root --password
//		USE mysql;
//		SELECT * FROM othello;
		
		
		//DB接続情報を設定する
        String path = "jdbc:mysql://localhost:3306/mysql";  //接続パス
        String id = "root";    //ログインID
        String pw = "root";  //ログインパスワード
      //SQL文を定義する
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
                    System.out.println("一時保存に失敗しました!");
                    throw e;
                }
                System.out.println("一時保存に成功しました!");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("一時保存に失敗しました!");
            }finally {
                System.out.println("終了!");
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
//            System.out.println("処理が成功しました!セックス!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("失敗!ファック!");
//        }finally {
//            System.out.println("終了!");
//        }
	}
}
