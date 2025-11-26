package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Music;
import model.Mutter;

public class MusicDAO {
	
	private final String JDBC_URL = "jdbc:mysql://localhost/musicon";
	private final String DB_USER = "root";
	private final String DB_PASS = "";

	public List<Music> searchMusic(String str_searchWord){
		List<Music> musicList = new ArrayList<>();
		// JDBCドライバを読み込む
				try{
					Class.forName("com.mysql.jdbc.Driver");
				}catch(ClassNotFoundException e) {
					throw new IllegalStateException("JDBCドライバを読み込めませんでした");
				}
				
				// データベース接続
				try(Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)){
					
					// SELECT文の準備（データベースからmusicsを取得）
					String sql = "SELECT * FROM MUSICS WHERE "; //<---ここから
					PreparedStatement pStmt = conn.prepareStatement(sql);
					
					// SELECT文を実行
					ResultSet rs = pStmt.executeQuery();
					
					// 結果をArrayListに格納
					while(rs.next()) {
						int id = rs.getInt("ID");
						String userName = rs.getString("NAME");
						String text = rs.getString("TEXT");
						// 新しいMutterオブジェクトを作成
						Mutter mutter = new Mutter(id, userName, text); 
						mutterList.add(mutter); // リストに追加
					}
				}catch(SQLException e) {
					e.printStackTrace(); // SQLエラーを表示
					return null;
				}
				return mutterList; // 全てのMutterリストを返す
			}
		}
	}
}
