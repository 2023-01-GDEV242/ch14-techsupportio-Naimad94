import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.Arrays;

/**
 * The responder class represents a response generator object.
 * It is used to generate an automatic response, based on specified input.
 * Input is presented to the responder as a set of words, and based on those
 * words the responder will generate a String that represents the response.
 *
 * Internally, the reponder uses a HashMap to associate words with response
 * strings and a list of default responses. If any of the input words is found
 * in the HashMap, the corresponding response is returned. If none of the input
 * words is recognized, one of the default responses is randomly chosen.
 * 
 * Damian Nunez (JonD)
 * February 24, 2023
 */
public class Responder
{
    // Used to map key words to responses.
    private HashMap<String, String> responseMap;
    // Default responses to use if we don't recognise a word.
    private ArrayList<String> defaultResponses;
    // The name of the file containing the default responses.
    private static final String FILE_OF_DEFAULT_RESPONSES = "default2.txt";
    
    // The name of the file containing the keys and values for the response map.
    private static final String FILE_OF_KEYS_AND_VALUES = "keyvalue.txt";
    
    private Random randomGenerator;

    /**
     * Construct a Responder
     */
    public Responder()
    {
        responseMap = new HashMap<String, String>();
        defaultResponses = new ArrayList<String>();
        fillResponseMap2(); //new method
        fillDefaultResponses2(); //new method
        randomGenerator = new Random();
    }

    /**
     * Generate a response from a given set of input words.
     * 
     * @param words  A set of words entered by the user
     * @return       A string that should be displayed as the response
     */
    public String generateResponse(HashSet<String> words)
    {
        Iterator<String> it = words.iterator();
        while(it.hasNext()) 
        {
            String word = it.next();
            String response = responseMap.get(word);
            if(response != null) 
            {
                return response;
            }
        }
        // If we get here, none of the words from the input line was recognized.
        // In this case we pick one of our default responses (what we say when
        // we cannot think of anything else to say...)
        return pickDefaultResponse();
    }

    /**
     * Enter all the known keywords and their associated responses
     * into our response map.
     */
    private void fillResponseMap()
    {
        responseMap.put("crash", 
                        "Well, it never crashes on our system. It must have something\n" +
                        "to do with your system. Tell me more about your configuration.");
        responseMap.put("crashes", 
                        "Well, it never crashes on our system. It must have something\n" +
                        "to do with your system. Tell me more about your configuration.");
        responseMap.put("slow", 
                        "I think this has to do with your hardware. Upgrading your processor\n" +
                        "should solve all performance problems. Have you got a problem with\n" +
                        "our software?");
        responseMap.put("performance", 
                        "Performance was quite adequate in all our tests. Are you running\n" +
                        "any other processes in the background?");
        responseMap.put("bug", 
                        "Well, you know, all software has some bugs. But our software engineers\n" +
                        "are working very hard to fix them. Can you describe the problem a bit\n" +
                        "further?");
        responseMap.put("buggy", 
                        "Well, you know, all software has some bugs. But our software engineers\n" +
                        "are working very hard to fix them. Can you describe the problem a bit\n" +
                        "further?");
        responseMap.put("windows", 
                        "This is a known bug to do with the Windows operating system. Please\n" +
                        "report it to Microsoft. There is nothing we can do about this.");
        responseMap.put("macintosh", 
                        "This is a known bug to do with the Mac operating system. Please\n" +
                        "report it to Apple. There is nothing we can do about this.");
        responseMap.put("expensive", 
                        "The cost of our product is quite competitive. Have you looked around\n" +
                        "and really compared our features?");
        responseMap.put("installation", 
                        "The installation is really quite straight forward. We have tons of\n" +
                        "wizards that do all the work for you. Have you read the installation\n" +
                        "instructions?");
        responseMap.put("memory", 
                        "If you read the system requirements carefully, you will see that the\n" +
                        "specified memory requirements are 1.5 giga byte. You really should\n" +
                        "upgrade your memory. Anything else you want to know?");
        responseMap.put("linux", 
                        "We take Linux support very seriously. But there are some problems.\n" +
                        "Most have to do with incompatible glibc versions. Can you be a bit\n" +
                        "more precise?");
        responseMap.put("bluej", 
                        "Ahhh, BlueJ, yes. We tried to buy out those guys long ago, but\n" +
                        "they simply won't sell... Stubborn people they are. Nothing we can\n" +
                        "do about it, I'm afraid.");
    }

    private void fillResponseMap2()
    {
        // for reference in populating a map:
        responseMap.put("crash", 
                        "Well, it never crashes on our system. It must have something\n" +
                        "to do with your system. Tell me more about your configuration.");
        responseMap.put("crashes", 
                        "Well, it never crashes on our system. It must have something\n" +
                        "to do with your system. Tell me more about your configuration.");
        
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_KEYS_AND_VALUES);
        try (Stream<String> stringStream = Files.lines(path, charset))
        {
            // convert the Stream of lines into a List
            ArrayList<String> dataFromFile = new ArrayList<String>(stringStream.collect(Collectors.toList()));
            
            // the population procedure depends on the list ending with an empty String
            // but the Stream would have eliminated any empty String line at the
            // end of the file. Therefore, make sure the list ends with an empty String
            dataFromFile.add("");
            
            // create an Iterator
            Iterator<String> it = dataFromFile.iterator();
            
            // initialize a newValue variable to the empty String
            String newValue = "";
            
            // initialize a keyList variable to hold key Strings
            List<String> keyList = new ArrayList<String>();
            
            // initialize a flag variable to aid in the iteration process
            boolean gotKeys = false;
            
            while(it.hasNext())
            {
                String text = it.next();
                
                if(!gotKeys)    // we are at a key element
                {
                    // separate the string into individual values, using the comma
                    // as the delimeter
                    String keyArray[] = text.split(",");
                    
                    // convert the array to an ArrayList
                    keyList = Arrays.asList(keyArray);
                    
                    // set the flag
                    gotKeys = true;
                }
                
                else        // we are not at a key element
                {
                    if(!text.equals(""))    // the element is NOT the empty String
                    {
                        // concatenete the next list element to the newValue String
                        newValue += text + "\n";
                    }
                    else        // the element IS the empty String
                    {
                        // make sure the newValue String isn't empty
                        if(newValue.length() > 0)
                        {
                            // add the value to the responseMap for each key in 
                            // the keyList, trimming any whitespace from the
                            // key(s) and the value
                            for(String key : keyList)
                            {
                                responseMap.put(key.trim(), newValue.trim());
                            }
                        }
                        
                        // reset the newValue String to empty
                        newValue = "";
                        
                        // reset the flag
                        gotKeys = false;
                    }
                }
            }
        }
        catch(FileNotFoundException e) 
        {
            System.err.println("Cannot open " + FILE_OF_KEYS_AND_VALUES);
        }
        catch(IOException e) 
        {
            System.err.println("Cannot read " +
                               FILE_OF_KEYS_AND_VALUES);
        }
    }

    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     * 
     * This is the original version of the method written by Barnes and Kölling.
     * It is here for reference.
     */
    private void fillDefaultResponses()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) 
        {
            String response = reader.readLine();
            while(response != null) 
            {
                defaultResponses.add(response);
                response = reader.readLine();
            }
        }
        catch(FileNotFoundException e) 
        {
            System.err.println("Cannot open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) 
        {
            System.err.println("Cannot read " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) 
        {
            defaultResponses.add("Please elaborate?");
        }
    }
    
    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     * This method parses an input text file in which responses are 
     * separated by a blank line in the file. A single response may 
     * take up multiple lines in the file.
     */
    private void fillDefaultResponses2()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        
        try (Stream<String> stringStream = Files.lines(path, charset))
        {
            // convert the Stream of lines into a List
            ArrayList<String> dataFromFile = new ArrayList<String>(
                stringStream.collect(Collectors.toList()));
            
            // the population procedure depends on the list ending with an empty String
            // but the Stream would have eliminated any empty String line at the
            // end of the file. Therefore, make sure the list ends with an empty String
            dataFromFile.add("");
            
            // create an Iterator
            Iterator<String> it = dataFromFile.iterator();
            
            // initialize a newResponse variable to the empty String
            String newResponse = "";
            
            while(it.hasNext())
            {
                String text = it.next();
                if(!text.equals(""))    // the next list element IS NOT the empty String
                {
                    // concatenate the next list element to the newResponse String
                    newResponse += text + " ";
                }
                else    // the next list element IS the empty String
                {
                    // this is where the method depends on the list ending with
                    // the empty String
                    // make sure the newResponse String isn't empty
                    if(newResponse.length() > 0)
                    {
                        // trim any trailing whitespace from the local String
                        // and add the local String to defaultResponses
                        defaultResponses.add(newResponse.trim());
                    }
                    
                    // reset the newResponse String to empty
                    newResponse = "";
                    
                    // move the pointer to the next element in the list
                    // the nature of the while loop handles this movement
                }
            }
        }
        catch(FileNotFoundException e) 
        {
            System.err.println("Cannot open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) {
            System.err.println("Cannot read " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) 
        {
            defaultResponses.add("Please Elaborate?");
        }
    }
    
    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     * This method explores the use of streams and lambdas and is not 
     * properly functional. It remains here for future code exploration
     * and debugging.
     */
    private void fillDefaultResponsesLambdaVersion()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        
        try (Stream<String> responseStream = Files.lines(path, charset))
        {
            responseStream.forEach(
                (response) -> 
                {
                    String nextResponse = new String("");
                    if(!response.equals(""))
                    {
                        nextResponse += response + " ";
                    }
                    if(!nextResponse.equals(""))
                    {
                        if(!nextResponse.equals(null))
                        {
                            defaultResponses.add(nextResponse);
                        }
                    }
                    nextResponse = "";
                }
            );
            // the arrayList should now have all the responses from the file
            // this works for single line responses only
            // I will explore this more in my own time where there isn't the pressure
            // to make a deadline
        }
        catch(FileNotFoundException e) 
        {
            System.err.println("Cannot open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) 
        {
            System.err.println("Cannot read " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) 
        {
            defaultResponses.add("Please Elaborate?");
        }
    }

    /**
     * Randomly select and return one of the default responses.
     * @return     A random default response
     */
    private String pickDefaultResponse()
    {
        // Pick a random number for the index in the default response list.
        // The number will be between 0 (inclusive) and the size of the list (exclusive).
        int index = randomGenerator.nextInt(defaultResponses.size());
        return defaultResponses.get(index);
    }
}