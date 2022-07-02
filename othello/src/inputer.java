package othello;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inputer {
	public static void main(String[] args) {
		
		//DB接続情報を設定する
        String path = "jdbc:mysql://localhost:3306/mysql";  //接続パス
        String id = "root";    //ログインID
        String pw = "root";  //ログインパスワード
        String sqlSave = "INSERT INTO othello VALUES(?, ?)";
        String sqlLoad = "SELECT * FROM othello";

		//初期設定
	    String player1 = "●";
	    String player2 = "○";
	    String nowplayer = player1;
	    String notplayer = player2;
	    String[][] board = new String[8][8];
	    for (int i = 0; i < board.length; i++) {
	    	for (int j = 0; j < board[i].length; j++) {	    	
	    		board[i][j] =  " ";
		    }
	    }
	    board[3][3] =  "○";
	    board[3][4] =  "●";
	    board[4][3] =  "●";
	    board[4][4] =  "○";
	    
	    while (true){
	    	
	    	//ボード表示処理
	    	char boardAlphabet[] = {'A','B','C','D','E','F','G','H'};
	    	System.out.println(" |1|2|3|4|5|6|7|8|");
	    	for (int i = 0; i < board.length; i++) {
	    		System.out.println(boardAlphabet[i] + "|" 
	    				+ board[i][0] + "|" + board[i][1] + "|" + board[i][2] + "|" + board[i][3] + "|" 
	    				+ board[i][4] + "|" + board[i][5] + "|" + board[i][6] + "|" + board[i][7] + "|");
	    	}
	    	
	    	//入力処理
	    	System.out.println(nowplayer + "の番です");
	    	System.out.println("英数字を入力してください↓↓（例：「A1」「P」でポーズ)");
		    Scanner scanner = new Scanner(System.in);
	    	String inputS = scanner.next();
	    	
	    	//入力判定処理
	    	boolean inputJudge = false;
	    	char judgeAlphabet[] = {'A','B','C','D','E','F','G','H'};
	    	char judgeAlphabetSmall[] = {'a','b','c','d','e','f','g','h'};
	    	char judgeNumber[] = {'1','2','3','4','5','6','7','8'};
	    	int putVertical = 0;
	    	int putBeside = 0;	
	    	if(inputS.length() == 2) {
	    		if (!Character.isDigit(inputS.charAt(0))) {
	    			for (int i = 0; i < judgeAlphabet.length; i++) {
	    		    	if (inputS.charAt(0) == judgeAlphabet[i] || inputS.charAt(0) == judgeAlphabetSmall[i]) {
	    		    		for (int j = 0; j < judgeNumber.length; j++) {
	    	    		    	if (inputS.charAt(1) == judgeNumber[j]) {
	    	    		    		inputJudge = true;
	    	    		    		putVertical = i;
	    	    			    	putBeside = j;
	    	    			    	break;
	    	    	            }
	    	    	    	}
	    	            }
	    	    	}
	            }else if (!Character.isDigit(inputS.charAt(1))) {
	            	for (int i = 0; i < judgeNumber.length; i++) {
	    		    	if (inputS.charAt(0) == judgeNumber[i]) {
	    		    		for (int j = 0; j < judgeAlphabet.length; j++) {
	    	    		    	if (inputS.charAt(1) == judgeAlphabet[j] || inputS.charAt(1) == judgeAlphabetSmall[j]) {
	    	    		    		inputJudge = true;
	    	    		    		putVertical = j;
	    	    			    	putBeside = i;
	    	    			    	break;
	    	    	            }
	    	    	    	}
	    	            }
	    	    	}
	            }
	    		if(!board[putVertical][putBeside].equals(" ")){
		    		System.out.println("そこに置くことはできません!");
		    		inputJudge = false;
		    		continue;
		    	}
	    	}else if(inputS.equals("P") || inputS.equals("p")) {
	    		Scanner scanner2 = new Scanner(System.in);
	    		System.out.println("ゲームを中断しますか?↓↓（「Y」はい「N」いいえ「P｝パス「S」一時保存「L」ロード）");
	    		String confirm = scanner2.next();
	    		if(confirm.equals("Y") || confirm.equals("y")) {
	    			break;
	    		}else if(confirm.equals("P") || confirm.equals("p")) {
	    			if(nowplayer.equals(player1)) {
						nowplayer = player2;
						notplayer = player1;
					}else if(nowplayer.equals(player2)){
						nowplayer = player1;
						notplayer = player2;
					}
	    			continue;
	    		}else if(confirm.equals("S") || confirm.equals("s")){
	    			String nowplayersave = "1";
	    			String saveDatas = "";
	    			if(nowplayer.equals(player2)) {
	    				nowplayersave = "2";
	    			}
	    			for (int i = 0; i < board.length; i++) {
	    		    	for (int j = 0; j < board[i].length; j++) {	    	
	    		    		if(board[i][j].equals(" ")) {
	    		    			saveDatas += 0;
	    					}else if(board[i][j].equals("●")){
	    						saveDatas += 1;
	    					}else if(board[i][j].equals("○")){
	    						saveDatas += 2;
	    					}
	    			    }
	    		    }
	    			try{
	    				Connection conn = DriverManager.getConnection(path, id, pw);
	                    conn.setAutoCommit(false);
	                    PreparedStatement ps = conn.prepareStatement(sqlSave);
                        ps.setString(1,nowplayersave);
                        ps.setString(2,saveDatas);
                        ps.executeUpdate();
                        conn.commit();
	                    System.out.println("一時保存に成功しました!");
	                }catch (Exception e){
	                    e.printStackTrace();
	                    System.out.println("一時保存に失敗しました!");
	                }
	    			continue;
	    		}else if(confirm.equals("L") || confirm.equals("l")){    			
	    			try{
	    				Connection con = DriverManager.getConnection(path, id, pw);
		    	        PreparedStatement pstmt = con.prepareStatement(sqlLoad);
		    	        ResultSet rs = pstmt.executeQuery();
		    	        String loadplayer = "1";
	    	            String loadData = null;
	    	            while (rs.next()) {
	    	            	loadplayer = rs.getString("name");
	    	            	loadData = rs.getString("data");
	    	            }
	    	            if(loadplayer.equals("1")) {
	    	            	nowplayer = player1;
							notplayer = player2;
		    			}else if(loadplayer.equals("2")) {
	    	            	nowplayer = player2;
							notplayer = player1;
		    			}
	    	            for (int i = 0 ,k = 0; i < board.length; i++) {
	    	    	    	for (int j = 0; j < board[i].length; j++) {
	    	    	    		if(loadData.charAt(k) == '0') {
	    	    	    			board[i][j] = " ";
		    					}else if(loadData.charAt(k) == '1'){
		    						board[i][j] = "●";
		    					}else if(loadData.charAt(k) == '2'){
		    						board[i][j] = "○";
		    					}
	    	    	    		k++;
	    	    		    }
	    	    	    }
	    	            System.out.println("ロードに成功しました!");
	    	        }catch (SQLException e) {
	    	            e.printStackTrace();
	    	            System.out.println("ロードに失敗しました!");
	    	        }
	    			continue;
	    		}
	    	}else{
	    		System.out.println("入力した値が違います!");
	    		continue;
	    	}
	    	
	    	//処理
	    	if(inputJudge) {
	    		//置き処理
	    		if(nowplayer.equals(player1)) {
	    			board[putVertical][putBeside] = player1;
					nowplayer = player2;
					notplayer = player1;
				}else if(nowplayer.equals(player2)){
					board[putVertical][putBeside] = player2;
					nowplayer = player1;
					notplayer = player2;
				}
	    		//リバーシ判定処理
	    		boolean judgeRight = false;
	    		int pointRight = 0;
	    		for(int i = putBeside + 1; i <= 7; i++) {
    				if(board[putVertical][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[putVertical][i].equals(notplayer)){
	    				if(i == putBeside + 1) {
	    					break;
	    				}else{
		    				judgeRight = true;
		    				pointRight = i;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeLeft = false;
	    		int pointLeft = 0;
	    		for(int i = putBeside - 1; i >= 0; i--) {
    				if(board[putVertical][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[putVertical][i].equals(notplayer)){
	    				if(i == putBeside - 1) {
	    					break;
	    				}else{
		    				judgeLeft = true;
		    				pointLeft = i;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeDown = false;
	    		int pointDown = 0;
	    		for(int i = putVertical + 1; i <= 7; i++) {
    				if(board[i][putBeside].equals(nowplayer)) {
    					continue;
	    			}else if(board[i][putBeside].equals(notplayer)){
	    				if(i == putVertical + 1) {
	    					break;
	    				}else{
	    					judgeDown = true;
	    					pointDown = i;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeUp = false;
	    		int pointUp = 0;
	    		for(int i = putVertical - 1; i >= 0; i--) {
    				if(board[i][putBeside].equals(nowplayer)) {
    					continue;
	    			}else if(board[i][putBeside].equals(notplayer)){
	    				if(i == putVertical - 1) {
	    					break;
	    				}else{
	    					judgeUp = true;
	    					pointUp = i;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeRightDown = false;
	    		int pointRightDown[] = {0,0};
	    		for(int i = putBeside + 1,j = putVertical + 1; i <= 7 && j <= 7; i++,j++) {
	    			if(board[j][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[j][i].equals(notplayer)){
	    				if(i == putBeside + 1 && j == putVertical + 1) {
	    					break;
	    				}else{
	    					judgeRightDown = true;
	    					pointRightDown[0] = i;
	    					pointRightDown[1] = j;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeLeftDown = false;
	    		int pointLeftDown[] = {0,0};
	    		for(int i = putBeside - 1,j = putVertical + 1; i >= 0 && j <= 7; i--,j++) {
	    			if(board[j][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[j][i].equals(notplayer)){
	    				if(i == putBeside - 1 && j == putVertical + 1) {
	    					break;
	    				}else{
	    					judgeLeftDown = true;
	    					pointLeftDown[0] = i;
	    					pointLeftDown[1] = j;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeRightUp = false;
	    		int pointRightUp[] = {0,0};
	    		for(int i = putBeside + 1,j = putVertical - 1; i <= 7 && j >= 0; i++,j--) {
	    			if(board[j][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[j][i].equals(notplayer)){
	    				if(i == putBeside + 1 && j == putVertical - 1) {
	    					break;
	    				}else{
	    					judgeRightUp = true;
	    					pointRightUp[0] = i;
	    					pointRightUp[1] = j;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		boolean judgeLeftUp = false;
	    		int pointLeftUp[] = {0,0};
	    		for(int i = putBeside - 1,j = putVertical - 1; i >= 0 && j >= 0; i--,j--) {
	    			if(board[j][i].equals(nowplayer)) {
    					continue;
	    			}else if(board[j][i].equals(notplayer)){
	    				if(i == putBeside - 1 && j == putVertical - 1) {
	    					break;
	    				}else{
	    					judgeLeftUp = true;
	    					pointLeftUp[0] = i;
	    					pointLeftUp[1] = j;
		    				break;
	    				}
	    			}else{
	    				break;
	    			}
	    		}
	    		//置く場所がない
	    		if(!judgeRight && !judgeLeft && !judgeDown && !judgeUp &&
	    		!judgeRightDown && !judgeLeftDown && !judgeRightUp && !judgeLeftUp) {
	    			System.out.println("そこに置くことはできません!");
	    			if(nowplayer.equals(player1)) {
		    			board[putVertical][putBeside] = " ";
						nowplayer = player2;
						notplayer = player1;
					}else if(nowplayer.equals(player2)){
						board[putVertical][putBeside] = " ";
						nowplayer = player1;
						notplayer = player2;
					}
	    			continue;
	    		}
	    			
	    		
	    		//リバーシ処理
	    		if(judgeRight) {
	    			for(int i = putBeside + 1; i <= pointRight; i++) {
	    				board[putVertical][i] = notplayer;
	    			}
	    		}
	    		if(judgeLeft) {
	    			for(int i = putBeside - 1; i >= pointLeft; i--) {
	    				board[putVertical][i] = notplayer;
	    			}
	    		}
	    		if(judgeDown) {
	    			for(int i = putVertical + 1; i <= pointDown; i++) {
	    				board[i][putBeside] = notplayer;
	    			}
	    		}
	    		if(judgeUp) {
	    			for(int i = putVertical - 1; i >= pointUp; i--) {
	    				board[i][putBeside] = notplayer;
	    			}
	    		}
	    		if(judgeRightDown) {
	    			for(int i = putBeside + 1,j = putVertical + 1; i <= pointRightDown[0] && j <= pointRightDown[1]; i++,j++) {
	    				board[j][i] = notplayer;
	    			}
	    		}
	    		if(judgeLeftDown) {
	    			for(int i = putBeside - 1,j = putVertical + 1; i >= pointLeftDown[0] && j <= pointLeftDown[1]; i--,j++) {
	    				board[j][i] = notplayer;
	    			}
	    		}
	    		if(judgeRightUp) {
	    			for(int i = putBeside + 1,j = putVertical - 1; i <= pointRightUp[0] && j >= pointRightUp[1]; i++,j--) {
	    				board[j][i] = notplayer;
	    			}
	    		}
	    		if(judgeLeftUp) {
	    			for(int i = putBeside - 1,j = putVertical - 1; i >= pointLeftUp[0] && j >= pointLeftUp[1]; i--,j--) {
	    				board[j][i] = notplayer;
	    			}
	    		}

//	    		System.out.println("judgeRightDown:"+judgeRightDown+" pointRightDown[0]:"+pointRightDown[0]+" pointRightDown[1]:"+pointRightDown[1]);
//	    		System.out.println("judgeLeftDown:"+judgeLeftDown+" pointLeftDown[0]:"+pointLeftDown[0]+" pointLeftDown[1]:"+pointLeftDown[1]);
//	    		System.out.println("judgeRightUp:"+judgeRightUp+" pointRightUp[0]:"+pointRightUp[0]+" pointRightUp[1]:"+pointRightUp[1]);
//	    		System.out.println("judgeLeftUp:"+judgeLeftUp+" pointLeftUp[0]:"+pointLeftUp[0]+" pointLeftUp[1]:"+pointLeftUp[1]);
	    		
	    		//勝敗判定
	    		boolean settleFull = true;
	    		OUT:
	    		for (int i = 0; i < board.length; i++) {
	    	    	for (int j = 0; j < board[i].length; j++) {	    	
	    	    		if(board[i][j].equals(" ")) {
	    	    			settleFull = false;
	    	    			break OUT;
	    	    		}
	    		    }
	    	    }
	    		boolean settle1 = true;
	    		OUT1:
	    		for (int i = 0; i < board.length; i++) {
	    	    	for (int j = 0; j < board[i].length; j++) {	    	
	    	    		if(board[i][j].equals(player1)) {
	    	    			settle1 = false;
	    	    			break OUT1;
	    	    		}
	    		    }
	    	    }
	    		boolean settle2 = true;
	    		OUT2:
	    		for (int i = 0; i < board.length; i++) {
	    	    	for (int j = 0; j < board[i].length; j++) {	    	
	    	    		if(board[i][j].equals(player2)) {
	    	    			settle2 = false;
	    	    			break OUT2;
	    	    		}
	    		    }
	    	    }			
	    		if(settleFull || settle1 || settle2) {
	    			//ボード表示処理
	    	    	System.out.println(" |1|2|3|4|5|6|7|8|");
	    	    	for (int i = 0; i < board.length; i++) {
	    	    		System.out.println(boardAlphabet[i] + "|" 
	    	    				+ board[i][0] + "|" + board[i][1] + "|" + board[i][2] + "|" + board[i][3] + "|" 
	    	    				+ board[i][4] + "|" + board[i][5] + "|" + board[i][6] + "|" + board[i][7] + "|");
	    	    	}
	    			int player1Result = 0;
	    			int player2Result = 0;
	    			for (int i = 0; i < board.length; i++) {
		    	    	for (int j = 0; j < board[i].length; j++) {	    	
		    	    		if(board[i][j].equals(player1)) {
		    	    			player1Result++;
		    	    		}else if(board[i][j].equals(player2)) {
		    	    			player2Result++;
		    	    		}
		    		    }
		    	    }
	    			System.out.println("●:"+player1Result+" 　○:"+player2Result);
	    			if(player1Result > player2Result) {
	    				System.out.println("おめでとう!●の勝ちです!!");
	    			}else if(player1Result < player2Result) {
	    				System.out.println("おめでとう!○の勝ちです!!");
	    			}else if(player1Result == player2Result) {
	    				System.out.println("ドロー!引き分けです!!");
	    			}
	    			while(true) {
		    			Scanner scanner3 = new Scanner(System.in);
			    		System.out.println("もう一度プレイしますか?↓↓（「Y」はい「N」いいえ）");
			    		String confirm = scanner3.next();
			    		if(confirm.equals("Y") || confirm.equals("y")) {
			    			for (int i = 0; i < board.length; i++) {
			    		    	for (int j = 0; j < board[i].length; j++) {	    	
			    		    		board[i][j] =  " ";
			    			    }
			    		    }
			    		    board[3][3] =  "○";
			    		    board[3][4] =  "●";
			    		    board[4][3] =  "●";
			    		    board[4][4] =  "○";
			    		    break;
			    		}else {
			    			continue;
			    		}
	    			}
	    		}
	    		
		    }else {
		    	System.out.println("入力した値が違います!");
	    	}
		}
	}
}
