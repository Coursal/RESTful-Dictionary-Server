package web_services;

public class Session_User 
{
    private String username;
    private String password;
    private String token;
    
    private static final int DURATION = 90000;  //token's active state duration, 90 seconds
    private static long token_timer = Long.MAX_VALUE;
    
    Session_User()
    {
        username = "";
        password = "";
        token = "";
    }
    
    
    public void activate_token_timer()
    {
        token_timer = System.currentTimeMillis();
    }
    
    
    public boolean is_token_valid()
    {
        long time_passed = System.currentTimeMillis() - token_timer;
        
        System.out.println("======================");
        System.out.println("Time passed: " + time_passed + " ms");
        System.out.println("======================");
        
        return time_passed>=0 && time_passed<=DURATION;
    }

    
    
    public void set_username(String u)
    {
        username = u;
    }
    
    public void set_password(String p)
    {
        password = p;
    }
    
    public void set_token(String t)
    {
        token = t;
    }
    
    
    public String get_username()
    {
        return username;
    }
    
    public String get_password()
    {
        return password;
    }
    
    public String get_token()
    {
        return token;
    }
}
