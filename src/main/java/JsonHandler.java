import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHandler {

    public static String userToJson(User usr) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(usr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
