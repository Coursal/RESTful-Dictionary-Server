package web_services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Path("web_services")
public class Services 
{
    static Session_User user = new Session_User();
    
    @Context
    private UriInfo context;

    public Services() {}
    
    //function to check a user's credentials in a database and return a token that's valid for 90 seconds
    @GET
    @Path("{username}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String Login(@PathParam ("username") String username, @PathParam ("password") String password) 
    {
        JSONObject message = new JSONObject();
        
        boolean authentication_flag = false;
        
        String token = "";

        //checking if username and password are in the users' database...
        try 
        {
            authentication_flag = authenticate_user(username,password);
        } 
        catch(ClassNotFoundException ex) 
        {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!authentication_flag)
        {
            token = "#ERROR#";
            message.put("token", token);
        }
        else
        {
            //creating the token by 30 random chars
            String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder builder = new StringBuilder(30);
            
            for(int i=0;i<30;i++)
                builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            
            token = builder.toString();
            message.put("token", token);
            
            user.set_username(username);
            user.set_password(password);
            user.set_token(token);
            
            user.activate_token_timer();  
        }
        
        return message.toString();
    }
    
    public boolean authenticate_user(String username, String password) throws ClassNotFoundException
    {
        boolean flag = true;
        
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String db_URL = "jdbc:mysql://127.0.0.1:3306/users_db?characterEndoding=utf8";
        String db_USERNAME = "admin";
        String db_PASSWORD = "admin";
        
        Connection db_connection = null;
        Statement db_statement = null;
        
        try
        {
            Class.forName(JDBC_DRIVER);
            db_connection = DriverManager.getConnection(db_URL, db_USERNAME, db_PASSWORD);

            if(db_connection != null)
            {
                    db_statement = db_connection.createStatement();

                    String sql_select = "SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"';";
                       
                    ResultSet results = db_statement.executeQuery(sql_select);
                    
                    if(!results.isBeforeFirst())
                        flag = false;
            }
        }
        catch(SQLException e)
        {
                db_connection = null;
                System.out.println("SQLException caught: " + e.getMessage());
        }
        
        return flag;
    }
    
    
    //function to check if a user's token has expired or not and returns words from a dictionary in the database that start with one letter and end with another
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String Words(@QueryParam ("token") String token, @QueryParam ("first_letter") String first_letter, @QueryParam ("last_letter") String last_letter) throws ClassNotFoundException 
    {
        JSONObject message = new JSONObject();
        JSONArray word_list = new JSONArray();

        if(user.is_token_valid())
        {
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String db_URL = "jdbc:mysql://127.0.0.1:3306/users_db?characterEndoding=utf8";
            String db_USERNAME = "admin";
            String db_PASSWORD = "admin";

            Connection db_connection = null;
            Statement db_statement = null;

            try
            {
                Class.forName(JDBC_DRIVER);
                db_connection = DriverManager.getConnection(db_URL, db_USERNAME, db_PASSWORD);

                if(db_connection != null)
                {
                    db_statement = db_connection.createStatement();

                    String sql_select = "SELECT * FROM dictionary WHERE word LIKE '"+first_letter+"%"+last_letter+"';"; 

                    ResultSet results = db_statement.executeQuery(sql_select);

                    while (results.next()) 
                    {
                        String word = results.getString("word");
                        word_list.add(word);     
                    }
                }
            }
            catch(SQLException e)
            {
                    db_connection = null;
                    System.out.println("SQLException caught: " + e.getMessage());
            }

            message.put("user", user.get_username());
            message.put("words", word_list);
        }
        else
        {
            message.put("user", "#Permission Denied#");   
            message.put("words", new JSONArray());
        }
            
        return message.toString();
    }

    /**
     * PUT method for updating or creating an instance of Services
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {}
}
