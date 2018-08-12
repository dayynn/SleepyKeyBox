import java.sql.*;
import java.util.ArrayList;
import java.util.MissingResourceException;

public class DBUtil {
    private static DBUtil singleton = null;

    private final String AUTHBASIC = "BASIC";

    public static DBUtil getInstance() throws SQLException {
        if (singleton == null)
            singleton = new DBUtil();
        return singleton;
    }

    private final String user = "keybox";
    private final String pass = "filepwd <YOURPASSWORDHERE>";
    private final String url = "jdbc:h2:/home/keybox/KeyBox-jetty/jetty/keybox/WEB-INF/classes/keydb/keybox;CIPHER=AES;MV_STORE=TRUE";
    private Connection conn;
    private Statement stmt;

    private DBUtil() throws SQLException {
        conn = DriverManager.getConnection(url, user, pass);
        stmt = conn.createStatement();
    }

    private ArrayList<User> createUsersFromQuery(String query) {
        ArrayList<User> retval = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                String uname = rs.getString("USERNAME");
                String utype_str = rs.getString("USER_TYPE");
                UserType utype = (utype_str.equals("M")) ? UserType.FULL_ACCESS : UserType.ADMIN;
                String first = rs.getString("FIRST_NM");
                String last = rs.getString("LAST_NM");
                String email = rs.getString("EMAIL");
                int id = rs.getInt("ID");
                User usr = new User();
                usr.setEmail(email);
                usr.setFirst_name(first);
                usr.setLast_name(last);
                usr.setUsername(uname);
                usr.setUsertype(utype);
                usr.setId(id);
                retval.add(usr);
            }
        } catch (Exception e) {
            System.out.println("Failed to create a set of users!");
            e.printStackTrace();
        }

        return retval;
    }

    public ArrayList<User> getUsers(User user) throws Exception {
        String query = "select * from users";
        if (user == null) {
            return createUsersFromQuery(query);
        } else {
            query += " where ";
        }
        boolean all_empty = true;
        System.out.println(user);
        if (user.getEmail() != null) {
            query += "EMAIL='" + user.getEmail() + "' ";
            all_empty = false;
        }
        if (user.getFirst_name() != null) {
            query += "FIRST_NM='" + user.getFirst_name() + "' ";
            all_empty = false;
        }
        if (user.getLast_name() != null) {
            query += "LAST_NM='" + user.getLast_name() + "' ";
            all_empty = false;
        }
        if (user.getUsername() != null) {
            query += "USERNAME='" + user.getUsername() + "' ";
            all_empty = false;
        }

        if (all_empty) {
            throw new Exception("Bad user creation.");
        }

        return createUsersFromQuery(query);
    }

    public void createUser(User usr) throws MissingResourceException, SQLException {
        String query =
                "insert into users (first_nm, last_nm, email, username, auth_type, user_type, password, salt) values (";
        if (usr.getFirst_name() == null || usr.getLast_name() == null || usr.getEmail() == null
                || usr.getUsername() == null || usr.getPassword() == null) {

            throw new MissingResourceException("User object is not fully formed", "User.java", "KEY?");

        }
        query += "'" + usr.getFirst_name() + "', '" + usr.getLast_name() + "', '" + usr.getEmail() + "', '"
                + usr.getUsername() + "', '" + AUTHBASIC + "', '";

        if (usr.getUsertype().equals(UserType.FULL_ACCESS)) {
            query += "M'";
        } else {
            query += "A'";
        }
        // implement password hashing but for now just insert nulls -> see KeyBox source code
        // https://github.com/skavanagh/KeyBox/blob/master/src/main/java/com/keybox/manage/util/EncryptionUtil.java
        // this is now located in HashUtil.java -> generateSalt() and hash()
        query += ", '" + usr.getHash() + "', '" + usr.getSalt() + "')";

        stmt.execute(query);
    }

    public void deleteUser(User usr) throws  MissingResourceException, SQLException {
        if (usr.getId() == 0) {
            throw new MissingResourceException("Incorrect ID", "User.java", "Key?");
        }
        String del_query = "delete from users where ID='" + usr.getId() + "'";
        stmt.execute(del_query);
    }
}
