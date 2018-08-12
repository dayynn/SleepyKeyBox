public class User {
    private String username;
    private UserType usertype;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String salt;
    private String hash;
    private int id;

    public User() {
        username = new String();
        first_name = new String();
        last_name = new String();
        email = new String();
        password = new String();
        id = -1;
        usertype = null;
    }

    public boolean isEmptyQueryUser() {
        if (username == null && first_name == null && last_name == null && last_name == null && email == null) {
            return true;
        }
        return false;
    }


    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
        this.salt = HashUtil.generateSalt();
        this.hash = HashUtil.hash(this.password, this.salt);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }

    public String getHash() {
        return hash;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    private void setHash(String hash) {
        this.hash = hash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        String output = "====USER " + id + "====\n";
        output += "Email: " + email + "\n";
        output += "Name: " + first_name + " " + last_name + "\n";
        output += "Username: " + username + "\n";
        output += "Type: ";
        if (usertype != null) {
            if (usertype.equals(UserType.ADMIN)) {
                output += "Administrator\n";
            } else {
                output += "Full User\n";
            }
        }
        return output;
    }
}
