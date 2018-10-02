package edu.csupomona.cs480.controller;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;


//import org.apache.commons.math3.stat.FrequencyTest;
import com.google.common.collect.ImmutableMap;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import edu.csupomona.cs480.App;
import edu.csupomona.cs480.data.GpsProduct;
import edu.csupomona.cs480.data.User;
import edu.csupomona.cs480.data.provider.GpsProductManager;
import edu.csupomona.cs480.data.provider.UserManager;

import javax.imageio.ImageIO;

/**
 * This is the controller used by Spring framework.
 * <p>
 * The basic function of this controller is to map
 * each HTTP API Path to the correspondent method.
 *
 */

@RestController
public class WebController {

	/**
	 * When the class instance is annotated with
	 * {@link Autowired}, it will be looking for the actual
	 * instance from the defined beans.
	 * <p>
	 * In our project, all the beans are defined in
	 * the {@link App} class.
	 */
	@Autowired
	private UserManager userManager;
	@Autowired
	private GpsProductManager gpsProductManager;

	/**
	 * This is a simple example of how the HTTP API works.
	 * It returns a String "OK" in the HTTP response.
	 * To try it, run the web application locally,
	 * in your web browser, type the link:
	 * 	http://localhost:8080/cs480/ping
	 */
	@RequestMapping(value = "/cs480/ping", method = RequestMethod.GET)
	String healthCheck() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return "OK-CS480-Demo";
	}

	/**
         * This is a simple example of how the HTTP API works.
         * It returns a String "whaddup whadduppp" in the HTTP response.
         * To try it, run the web application locally,
         * in your web browser, type the link:
         *      http://localhost:8080/cs480/Amanda
         */
        @RequestMapping(value = "/cs480/Amanda", method = RequestMethod.GET)
        String whaddupStatus() {
                // You can replace this with other string,
                // and run the application locally to check your changes
                // with the URL: http://localhost:8080/
                return "whaddup whadduppp";
        }


        /**
         * This addition specifies the function from calling
         *      http://localhost:8080/cs480/Jacob
         * The method returns a string that describes this specific user
         */
        @RequestMapping(value = "/cs480/Jacob", method = RequestMethod.GET)
        String userDescrip() {
                return "\n\nI like Bacon, Purple, and Petre";
        }
        
        /**
         * This will be accessed through the link:
         *      http://localhost:8080/cs480/FoodFrequency
         * The method will eventually look at the number of times certain words are used
         
        @RequestMapping(value = "/cs480/Jacob", method = RequestMethod.GET)
        String foodFreq() {
        	
	        Frequency f = new Frequency();
	        f.addValue("steak");
	        f.addValue("brocolli");
	        f.addValue("mayonaise");
	        System.out.println(f.getCount("Steak"));
	        System.out.println(f.getCumPct("steak"));
	        System.out.println(f.getCumPct("mayo"));

                return "\nComplete";
        }
        */
        
	/**
	 * This is a simple example of how the HTTP API works.
	 * It returns a string that forms and disappears letter by letter in the HTTP response.
	 * To try it, run the web application locally.
	 * In your web browser, go to the link:
	 * 	http://localhost:8080/cs480/petre24
	 */
	@RequestMapping(value = "/cs480/petre24", method = RequestMethod.GET)
	String waffleFries() {
		return "W\r\nWa\r\nWaf\r\nWaff\r\nWaffl\r\nWaffle\r\nWaffle F\r\nWaffle Fr\r\n"
        		+ "Waffle Fri\r\nWaffle Frie\r\nWaffle Fries\r\nWaffle Frie\r\nWaffle Fri\r\n"
        		+ "Waffle Fr\r\nWaffle F\r\nWaffle\r\nWaffl\r\nWaff\r\nWaf\r\nWa\r\nW\r\n";	
	}	

	/**
	 * This is a simple example of how to use a data manager
	 * to retrieve the data and return it as an HTTP response.
	 * <p>
	 * Note, when it returns from the Spring, it will be
	 * automatically converted to JSON format.
	 * <p>
	 * Try it in your web browser:
	 * 	http://localhost:8080/cs480/user/user101
	 */

	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String handleFileUpload() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource("/test.jpg"));
		// BufferedImage image = ImageIO.read(new File("picture/test.jpg"));
		BufferedImage small = Scalr.resize(image,
				Scalr.Method.ULTRA_QUALITY,
				Scalr.Mode.AUTOMATIC,
				500, 500,
				Scalr.OP_ANTIALIAS);

		BufferedImage cropimage = Scalr.crop(image, 1000, 1000, Scalr.OP_ANTIALIAS);
		BufferedImage padding   = Scalr.pad(image, 200, Scalr.OP_DARKER);
		ImageIO.write(padding, "jpg", new File("test_thumb.jpg"));
		return "picture edited";
	}

	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.GET)
	User getUser(@PathVariable("userId") String userId) {
		User user = userManager.getUser(userId);
		return user;
	}

	/**
	 * This is an example of sending an HTTP POST request to
	 * update a user's information (or create the user if not
	 * exists before).
	 *
	 * You can test this with a HTTP client by sending
	 *  http://localhost:8080/cs480/user/user101
	 *  	name=John major=CS
	 *
	 * Note, the URL will not work directly in browser, because
	 * it is not a GET request. You need to use a tool such as
	 * curl.
	 *
	 * @param id
	 * @param name
	 * @param major
	 * @param age
	 * @return
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.POST)
	User updateUser(
			@PathVariable("userId") String id,
			@RequestParam("name") String name,
			@RequestParam(value = "major", required = false) String major,
			@RequestParam(value = "age", required = false) int age) {
		User user = new User();
		user.setId(id);
		user.setMajor(major);
		user.setName(name);
		user.setAge(age);
		userManager.updateUser(user);
		return user;
	}

	/**
	 * This API deletes the user. It uses HTTP DELETE method.
	 *
	 * @param userId
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.DELETE)
	void deleteUser(
			@PathVariable("userId") String userId) {
		userManager.deleteUser(userId);
	}

	/**
	 * This API lists all the users in the current database.
	 *
	 * @return
	 */
	@RequestMapping(value = "/cs480/users/list", method = RequestMethod.GET)
	List<User> listAllUsers() {
		return userManager.listAllUsers();
	}
	
	@RequestMapping(value = "/cs480/gps/list", method = RequestMethod.GET)
	List<GpsProduct> listGpsProducts() {
		return gpsProductManager.listAllGpsProducts();
	}
	
	/**
	 * This API displays an un-creative string.
	 *
	 * @return
	 */
	@RequestMapping(value = "/cs480/leslie", method = RequestMethod.GET)
	String UnCreative() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return "Wow, this is all I did";
	}
	
	/**
	 * Uses google guava to create an Immutable map, which creates a map 
	 * whose contents will not change. 
	 * Returns a string of the Map
	 * 
	 * @return
	 */	
	@RequestMapping(value = "/cs480/googleguava", method = RequestMethod.GET)
	String guavaTest() {
		Map<String, Integer> myDogs = ImmutableMap.of("Roxy", 11, "Bella", 6, "Waldo", 1);
		return myDogs.toString();
	}


	/*********** Web UI Test Utility **********/
	/**
	 * This method provide a simple web UI for you to test the different
	 * functionalities used in this web service.
	 */
	@RequestMapping(value = "/cs480/home", method = RequestMethod.GET)
	ModelAndView getUserHomepage() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("users", listAllUsers());
		return modelAndView;
	}

}
