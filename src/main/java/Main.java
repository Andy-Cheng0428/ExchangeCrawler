import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class Main {
    private static final String cmd = "py ";
    private static final String pyLocation = "\\src\\py";
    private static final Runtime runtime = Runtime.getRuntime();
    private static Process process;

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/db?serverTimezone=UTC";
    private static String userName = "Andy";
    private static String userPassword = "aa123456";
    private static PreparedStatement statement;

    public static void main(String[] args) {
        Timer timer = new Timer();


        File file = new File("");
        String rootPath = file.getAbsolutePath();
        String a = rootPath.concat(pyLocation);
        File pyDir = new File(a);
        Arrays.stream(pyDir.listFiles()).forEach(f -> {
            try {
                process = runtime.exec(String.format("%s%s", cmd, f.getAbsoluteFile()));
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "big5"));) {
                    String data = "";
                    final String[] tableName = new String[1];
                    String line;

                    int i = 0;
                    while ((line = in.readLine()) != null) {
                        if (i == 0) {
                            tableName[0] = line.toUpperCase(); //表格名稱
                        } else {
                            data = line; //儲存資料
                            System.out.println(URLDecoder.decode(line, "big5"));
                            Connection connection = null;
                            statement = null;
                            List<CurrEntity> currEntities = Singleton.getGson().fromJson(data, new TypeToken<List<CurrEntity>>() {
                            }.getType());
                            try {
                                connection = DriverManager.getConnection(url, userName, userPassword);
                                String sql = " create table if not exists " + tableName[0] + " (CURR char(3), SPOT_BUYING char(15), SPOT_SELLING char(15), CASH_BUYING char(15), CASH_SELLING char(15), TIMESTAMP char(20));";
                                System.out.println("sql = " + sql);
                                statement = connection.prepareStatement(sql);
                                statement.execute();
                                currEntities.forEach(currEntity -> {
                                    try {
                                        statement.addBatch(currEntity.genInserSql(tableName[0]));
                                        statement.executeBatch();
                                    } catch (SQLException throwables) {
                                        System.out.print("Exception " + throwables.getLocalizedMessage());
                                    }
                                });
                            } catch (Exception e) {
                                System.out.print("Exception " + e.getLocalizedMessage());
                            }
                        }
                        i++;
                    }
                } catch (Exception e) {
                    // 攔截IOException
                    System.out.print("Exception " + e.getLocalizedMessage());
                }
                process.waitFor();
            } catch (Exception e) {
                // 攔截process產生之Exception
                System.out.print("Exception " + e.getLocalizedMessage());
            }
        });


    }
}
