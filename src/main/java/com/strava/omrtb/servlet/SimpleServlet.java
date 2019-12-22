package com.strava.omrtb.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.model.StravaClub;
import javastrava.api.v3.rest.API;
import javastrava.api.v3.rest.AuthorisationAPI;
import javastrava.api.v3.service.Strava;

/**
 * Servlet implementation class SimpleServlet
 */
public class SimpleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/plain");
		resp.getWriter().append("Served at: ").append(req.getContextPath());
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//InputStream input = getServletContext().getResourceAsStream("/application.properties");
		InputStream input = classLoader.getResourceAsStream("application.properties");
		
		Properties prop = new Properties();
		prop.load(input);
		System.out.println("strava.client_id :: "+ prop.get("strava.client_id"));
		int clientId = Integer.parseInt((String) prop.get("strava.client_id"));
		String clientSecret = prop.get("strava.client_secret").toString();
		System.out.println("clientId :: "+clientId);
		System.out.println("clientSecret :: "+clientSecret);
		//TokenManager tokenManager= TokenManager.instance();
		//tokenManager.retrieveTokenWithScope("skumarvdlf@gmail.com");
		//if(tokenManager==null || tokenManager.ge)
		AuthorisationService service = new AuthorisationServiceImpl();
		String code = req.getParameter("code");
		System.out.println("Code :: " + code);
		//Token token = service.tokenExchange(clientId, clientSecret, code);
		
		AuthorisationAPI auth = API.authorisationInstance();
		TokenResponse response = auth.tokenExchange(clientId, clientSecret, code);
		Token token = new Token(response);
		
		//TokenManager.instance().storeToken(token);
		Strava strava = new Strava(token);
		StravaAthlete athlete = strava.getAuthenticatedAthlete();
		StravaClub club = strava.getClub(275482);
		resp.getWriter().write("Total members :: "+club.getMemberCount());
		System.out.println("First Name :: "+athlete.getFirstname());
		System.out.println("Last Name :: "+athlete.getLastname());
		System.out.println("email :: "+athlete.getEmail());
		resp.getWriter().write("Hello World! Maven Web Project Example.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
