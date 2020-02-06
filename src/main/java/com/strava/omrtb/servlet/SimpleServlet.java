package com.strava.omrtb.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javastrava.api.API;
import javastrava.api.AuthorisationAPI;
import javastrava.auth.AuthorisationService;
import javastrava.auth.impl.AuthorisationServiceImpl;
import javastrava.auth.model.Token;
import javastrava.auth.model.TokenResponse;
import javastrava.model.StravaActivity;
import javastrava.model.StravaAthlete;
import javastrava.service.ActivityService;
import javastrava.service.AthleteService;
import javastrava.service.WebhookService;
import javastrava.service.impl.ActivityServiceImpl;
import javastrava.util.Paging;

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
		System.out.println("Do Get method");
		resp.setContentType("text/plain");
        /*Enumeration<String> parameterNames = req.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
 
            String paramName = parameterNames.nextElement();
            System.out.println(paramName);
 
            String[] paramValues = req.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                System.out.println("\t" + paramValue);
            }
 
        }*/
 

		//resp.getWriter().append("Served at: ").append(req.getContextPath());
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//InputStream input = getServletContext().getResourceAsStream("/application.properties");
		InputStream input = classLoader.getResourceAsStream("application.properties");
		
		Properties prop = new Properties();
		prop.load(input);
		//System.out.println("strava.client_id :: "+ prop.get("strava.client_id"));
		int clientId = Integer.parseInt((String) prop.get("strava.client_id"));
		String clientSecret = prop.get("strava.client_secret").toString();
		System.out.println("clientId :: "+clientId);
		System.out.println("clientSecret :: "+clientSecret);
		//TokenManager tokenManager= TokenManager.instance();
		//tokenManager.retrieveTokenWithScope("skumarvdlf@gmail.com");
		//if(tokenManager==null || tokenManager.ge)
		//AuthorisationService service = new AuthorisationServiceImpl();
		String code = req.getParameter("code");
		//System.out.println("Code :: " + code);
		//Token token = service.tokenExchange(clientId, clientSecret, code);
		
		MyStravaAuthorisationAPI myAuth = MyStravaAPI.authorisationInstanceMy();
		MyStravaTokenResponse response = null;
		try {
			response = myAuth.tokenExchangeMyStrava(clientId, clientSecret, code, "authorization_code");
		}
		catch (Exception e) {
			MyStravaRefreshTokenAPI refersh = MyStravaAPI.authorisationInstanceMyRefresh();
			response = refersh.tokenExchangeMyStravaRefresh(clientId, clientSecret, "refresh_token", "862b16f24f8f3300caacbc6d2f1c5918adf3f820");
		}
		
		//AuthorisationAPI auth = API.authorisationInstance();
		//TokenResponse response = auth.tokenExchange(clientId, clientSecret, code, "authorization_code");
		System.out.println("respn :: "+response.getTokenType()+", "+response.getRefreshToken()+", "+response.getAccessToken());
		response.getTokenType();
		Token token = new Token(response);
		AthleteService asss = token.getService(AthleteService.class);
		ActivityService sa = ActivityServiceImpl.instance(token);
		//Token token = service.tokenExchange(clientId, clientSecret, code);
		Paging page = new Paging(0,20);
		WebhookService ts = token.getService(WebhookService.class);
		
		//TokenManager.instance().storeToken(token);
		/*Strava strava = new Strava(token);
		StravaAthlete athlete = strava.getAthlete(36145308);
		StravaClub club = strava.getClub(275482);
		ClubService cs = ClubServiceImpl.instance(token);
		List<StravaAthlete> clm = cs.listAllClubAdmins(275482);
		for (StravaAthlete stravaAthlete : clm) {
			System.out.println(stravaAthlete.getEmail()+", "+stravaAthlete.getFirstname()+", "+stravaAthlete.getLastname());
		}
		List<StravaActivity> dsds = cs.listRecentClubActivities(275482, page);
		for (StravaActivity stravaActivity : dsds) {
			System.out.print("Athlete "+stravaActivity.getAthlete().getId()+", "+stravaActivity.getAthlete().getFirstname()+", "+stravaActivity.getAthlete().getLastname());
			System.out.print(", "+stravaActivity.getName());
			System.out.print(",  "+(stravaActivity.getWorkoutType()==null?"":stravaActivity.getWorkoutType().getDescription()));
			System.out.print(", "+(stravaActivity.getElapsedTime()==null?0:stravaActivity.getElapsedTime()/60));
			System.out.print(", "+stravaActivity.getStartDateLocal());
			System.out.print(", "+stravaActivity.getMovingTime());
			System.out.print(", "+stravaActivity.getDistance());
			System.out.print(", "+stravaActivity.getTotalElevationGain());
			System.out.println(", "+(stravaActivity.getType()==null?"":stravaActivity.getType().getDescription()));
		}
		resp.getWriter().write("Total members :: "+club.getMemberCount());
		System.out.println("First Name :: "+athlete.getFirstname());
		System.out.println("Last Name :: "+athlete.getLastname());
		System.out.println("email :: "+athlete.getEmail());*/
		resp.getWriter().write("Hello World! Maven Web Project Example.");
		LocalDateTime before = LocalDateTime.now();
		LocalDateTime after = LocalDateTime.of(2019,Month.JANUARY,1,0,0);
		List<StravaActivity>  ll = sa.listAuthenticatedAthleteActivities(before, after, page);
		for (StravaActivity stravaActivity : ll) {
			System.out.println("What is :: "+stravaActivity.getId()+", "+stravaActivity.getName()+",  "+stravaActivity.getWorkoutType().getDescription()+", "+stravaActivity.getElapsedTime()/60 + ", "+stravaActivity.getStartDateLocal()+", "+stravaActivity.getType().getDescription());
		}
		
		AuthorisationService service = new AuthorisationServiceImpl();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Do Post method");
		doGet(request, response);
	}

}
