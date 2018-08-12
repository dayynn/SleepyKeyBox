import java.util.ArrayList;
import static spark.Spark.*;



public class Main {

    private static final String EMAILAPI = "email";
    private static final String UNAMEAPI = "username";
    private static final String FNAMEAPI = "first_name";
    private static final String LNAMEAPI = "last_name";
    private static final String UIDAPI = "user_id";
    private static final String PWORDAPI = "password";
    private static final String AUTHAPI = "auth";
    private static final String USERAPI = "user_type";

    public static void main(String[] args) {
        //do secure connection stuff here if needed
        path("/api", () -> {
            get("/users", (req, res) -> {
                System.out.println("Accessing database to find user");
                String email = req.queryParams(EMAILAPI);
                String username = req.queryParams(UNAMEAPI);
                String fname = req.queryParams(FNAMEAPI);
                String lname = req.queryParams(LNAMEAPI);

                User usr = new User();
                usr.setEmail(email);
                usr.setUsername(username);
                usr.setLast_name(lname);
                usr.setFirst_name(fname);
                if (usr.isEmptyQueryUser()) {
                    usr = null;
                }

                String retval = "";

                try {
                    DBUtil db = DBUtil.getInstance();
                    ArrayList<User> users = db.getUsers(usr);

                    for (User user : users) {
                        retval += JsonHandler.userToJson(user) + "\n";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    retval += "Could not access the database!";
                }
                return retval;
            });



            post("/newuser", (req, res) -> {
                System.out.println("Creating a new user based on parameters");
                String email = req.queryParams(EMAILAPI);
                String username = req.queryParams(UNAMEAPI);
                String fname = req.queryParams(FNAMEAPI);
                String lname = req.queryParams(LNAMEAPI);
                String pword = req.queryParams(PWORDAPI);
                String utype_raw = req.queryParams(USERAPI);
                UserType utype = UserType.ADMIN;
                if (utype_raw.equals(UserType.FULL_ACCESS.toString())) {
                    utype = UserType.FULL_ACCESS;
                }

                User usr = new User();
                usr.setEmail(email);
                usr.setUsername(username);
                usr.setLast_name(lname);
                usr.setFirst_name(fname);
                usr.setPassword(pword);
                usr.setUsertype(utype);
                if (usr.isEmptyQueryUser()) {
                    System.out.println("Invalid parameters specified");
                }

                String retval = "";

                try {
                    DBUtil db = DBUtil.getInstance();
                    db.createUser(usr);
                    retval += "Created user: " + fname + " " + lname;
                } catch (Exception e) {
                    e.printStackTrace();
                    retval += "Could not access the database!";
                }
               return retval;
            });


            post("/deleteuser", (req, res) -> {
                System.out.println("Removing a user based on UID");
                int uid = Integer.parseInt(req.queryParams(UIDAPI));

                User usr = new User();
                usr.setId(uid);

                String retval = "";
                try {
                    DBUtil db = DBUtil.getInstance();
                    db.deleteUser(usr);
                    retval += "Deleted user successfully";
                } catch (Exception e) {
                    e.printStackTrace();
                    retval += "Unable to delete user!";
                }
                return retval;
            });
        });
    }
}
