package uk.co.malbec.onlinebankingexample;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Service;
import uk.co.malbec.onlinebankingexample.model.RecentPayment;
import uk.co.malbec.onlinebankingexample.model.User;

import java.io.IOException;
import java.util.*;

import static org.joda.time.DateTime.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;

@Service
public class BackendService {

    private Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());

    {
        Map user = new HashMap<String, Object>() {{
            put("username", "robindevilliers");
            put("password", "mysecret");
            put("challengePhrase", "one");
            //put("challengePhrase","onceuponamidnightdreary");

            put("personalDetails", new HashMap<String, String>() {{
                put("name", "Robin de Villiers");
                put("nationality", "British");
                put("domicile", "UK");
                put("address", "7 Special Way, FairBank, ImaginaryVille, WOW007");
                put("mobile", "0788 1234 567");
                put("email", "robin@imaginaryville.co.uk");
            }});

            put("standingOrders", new ArrayList<Map>() {{
                add(new HashMap<String, String>() {{
                    put("id", "1");
                    put("dueDate", "30 Aug 2014");
                    put("description", "Lorem ipsum dolor sit amet.");
                    put("reference", "10001-02203-222");
                    put("period", "Monthly");
                    put("amount", "2343");
                    put("accountNumber", "93841732");
                    put("sortCode", "893810");
                }});
                add(new HashMap<String, String>() {{
                    put("id", "2");
                    put("dueDate", "30 Sep 2014");
                    put("description", "Sed consequat");
                    put("reference", "X12345-ROBIN-DE-VILLIERS");
                    put("period", "Quarterly");
                    put("amount", "1500");
                    put("accountNumber", "44433232");
                    put("sortCode", "893810");
                }});
                add(new HashMap<String, String>() {{
                    put("id", "3");
                    put("dueDate", "2 Dec 2014");
                    put("description", "Etiam sit amet ");
                    put("reference", "11102929920293837X");
                    put("period", "Yearly");
                    put("amount", "8500");
                    put("accountNumber", "23334332");
                    put("sortCode", "100210");
                }});
            }});

            put("recentPayments", new ArrayList<Map<String, String>>(){{
                add(new HashMap<String, String>(){{
                    put("date", "30 Aug 2014");
                    put("description","Lorem ipsum dolor sit amet.");
                    put("reference","1112");
                    put("accountNumber", "93841732");
                    put("sortCode", "893810");
                    put("amount","2343");
                    put("cleared", "");
                }});

                add(new HashMap<String, String>(){{
                    put("date", "29 Aug 2014");
                    put("description","Aenean imperdiet");
                    put("reference","111223");
                    put("accountNumber", "12345678");
                    put("sortCode", "100102");
                    put("amount","50000");
                    put("cleared", "");
                }});
                add(new HashMap<String, String>(){{
                    put("date", "30 May 2014");
                    put("description","Sed consequat");
                    put("reference","1113");
                    put("accountNumber", "44433232");
                    put("sortCode", "893810");
                    put("amount","1500");
                    put("cleared", "2 Jun 2014");
                }});
                add(new HashMap<String, String>(){{
                    put("date", "2 Dec 2014");
                    put("description","Etiam sit amet ");
                    put("reference","1114");
                    put("accountNumber", "23334332");
                    put("sortCode", "100210");
                    put("amount","8500");
                    put("cleared", "4 Dec 2014");
                }});

            }});
            put("accounts", new ArrayList<Map>() {{
                add(new HashMap<String, Object>() {{
                    put("name", "Premium Current Account");
                    put("type", "Current");
                    put("number", "1");
                    put("balance", "867578");
                    put("transactions", new ArrayList<Map>() {{
                        add(new HashMap<String, String>() {{
                            put("date", "30 Aug 2014");
                            put("description", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit");
                            put("type", "DEBIT");
                            put("amount", "2343");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "27 Aug 2014");
                            put("description", "Aenean commodo ligula eget dolor");
                            put("type", "CREDIT");
                            put("amount", "349");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "14 Aug 2014");
                            put("description", "Aenean massa");
                            put("type", "DEBIT");
                            put("amount", "231");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "12 Aug 2014");
                            put("description", "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus");
                            put("type", "DEBIT");
                            put("amount", "2353");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "10 Aug 2014");
                            put("description", "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem");
                            put("type", "CREDIT");
                            put("amount", "3213");
                        }});
                    }});
                }});
                add(new HashMap<String, Object>() {{
                    put("name", "Easy Saver Account");
                    put("type", "Saver");
                    put("number", "2");
                    put("balance", "100000");
                    put("transactions", new ArrayList<Map>() {{
                        add(new HashMap<String, String>() {{
                            put("date", "30 Aug 2014");
                            put("description", "Nulla consequat massa quis enim");
                            put("type", "DEBIT");
                            put("amount", "15000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "27 Aug 2014");
                            put("description", "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu");
                            put("type", "CREDIT");
                            put("amount", "15000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "14 Aug 2014");
                            put("description", "In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo");
                            put("type", "DEBIT");
                            put("amount", "30000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "12 Aug 2014");
                            put("description", "Nullam dictum felis eu pede mollis pretium");
                            put("type", "DEBIT");
                            put("amount", "20000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "10 Aug 2014");
                            put("description", "Integer tincidunt");
                            put("type", "CREDIT");
                            put("amount", "10000");
                        }});
                    }});
                }});
                add(new HashMap<String, Object>() {{
                    put("name", "Fancy Mortgage");
                    put("type", "Mortgage");
                    put("number", "3");
                    put("balance", "-15600000");
                    put("transactions", new ArrayList<Map>() {{
                        add(new HashMap<String, String>() {{
                            put("date", "25 Aug 2014");
                            put("description", "Cras dapibus");
                            put("type", "CREDIT");
                            put("amount", "135000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "25 Jul 2014");
                            put("description", "Vivamus elementum semper nisi");
                            put("type", "DEBIT");
                            put("amount", "115000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "25 Jun 2014");
                            put("description", "Aenean vulputate eleifend tellus");
                            put("type", "CREDIT");
                            put("amount", "135000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "25 May 2014");
                            put("description", "Sed consequat, leo eget bibend");
                            put("type", "DEBIT");
                            put("amount", "115000");
                        }});
                        add(new HashMap<String, String>() {{
                            put("date", "25 Aug 2014");
                            put("description", "Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim");
                            put("type", "CREDIT");
                            put("amount", "135000");
                        }});
                    }});
                }});
            }});

        }};

        //temporary test data.
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                    if (jp.getText().trim().isEmpty()){
                        return null;
                    }
                    return parse(jp.getText(), forPattern("dd MMM yyyy")).toDate();
                }
            });
            mapper.registerModule(module);

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

    public User getUser(String username) {
        return users.get(username);
    }
}
