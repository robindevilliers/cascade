package uk.co.malbec.onlinebankingexample;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import uk.co.malbec.onlinebankingexample.model.User;

import java.io.IOException;
import java.util.*;

@Service
public class BackendService {

    private Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());


    {
        Map user = new HashMap<String, Object>(){{
            put("username", "robindevilliers");
            put("password", "mysecret");
            put("challengePhrase","one");
            put("accounts", new ArrayList<Map>(){{
                add(new HashMap<String,Object>(){{
                    put("name","Premium Current Account");
                    put("number", "1");
                    put("balance", "10");
                    put("transactions", new ArrayList<Map>(){{
                        add(new HashMap<String, String>(){{
                            put("date","30 Aug 2014");
                            put("description","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor");
                            put("type","DEBIT");
                            put("amount","2343");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","27 Aug 2014");
                            put("description","In enim justo, rhoncus ut,");
                            put("type","CREDIT");
                            put("amount","349");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","14 Aug 2014");
                            put("description","Curabitur ullamcorper ultricies nisi.");
                            put("type","DEBIT");
                            put("amount","231");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","12 Aug 2014");
                            put("description","Sed consequat, leo eget bibend");
                            put("type","DEBIT");
                            put("amount","2353");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","10 Aug 2014");
                            put("description"," Duis leo.");
                            put("type","CREDIT");
                            put("amount","3213");
                        }});
                    }});
                }});
                add(new HashMap<String,Object>(){{
                    put("name","Easy Saver Account");
                    put("number", "2");
                    put("balance", "20");
                    put("transactions", new ArrayList<Map>(){{
                        add(new HashMap<String, String>(){{
                            put("date","30 Aug 2014");
                            put("description","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor");
                            put("type","DEBIT");
                            put("amount","2343");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","27 Aug 2014");
                            put("description","In enim justo, rhoncus ut,");
                            put("type","CREDIT");
                            put("amount","349");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","14 Aug 2014");
                            put("description","Curabitur ullamcorper ultricies nisi.");
                            put("type","DEBIT");
                            put("amount","231");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","12 Aug 2014");
                            put("description","Sed consequat, leo eget bibend");
                            put("type","DEBIT");
                            put("amount","2353");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","10 Aug 2014");
                            put("description"," Duis leo.");
                            put("type","CREDIT");
                            put("amount","3213");
                        }});
                    }});
                }});
                add(new HashMap<String,Object>(){{
                    put("name","Fancy Offset Mortgage");
                    put("number", "3");
                    put("balance", "30");
                    put("transactions", new ArrayList<Map>(){{
                        add(new HashMap<String, String>(){{
                            put("date","30 Aug 2014");
                            put("description","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor");
                            put("type","DEBIT");
                            put("amount","2343");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","27 Aug 2014");
                            put("description","In enim justo, rhoncus ut,");
                            put("type","CREDIT");
                            put("amount","349");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","14 Aug 2014");
                            put("description","Curabitur ullamcorper ultricies nisi.");
                            put("type","DEBIT");
                            put("amount","231");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","12 Aug 2014");
                            put("description","Sed consequat, leo eget bibend");
                            put("type","DEBIT");
                            put("amount","2353");
                        }});
                        add(new HashMap<String, String>(){{
                            put("date","10 Aug 2014");
                            put("description"," Duis leo.");
                            put("type","CREDIT");
                            put("amount","3213");
                        }});
                    }});
                }});
            }} );
            //put("challengePhrase","onceuponamidnightdreary");
        }};

        //temporary test data.
        try {
            ObjectMapper mapper = new ObjectMapper();
            User robin = mapper.readValue(mapper.writeValueAsString(user), User.class);
            setUser(robin);
        } catch (JsonProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public void setUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username){
        return users.get(username);
    }
}
