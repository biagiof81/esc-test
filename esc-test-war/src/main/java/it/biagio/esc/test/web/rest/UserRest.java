package it.biagio.esc.test.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import it.biagio.esc.test.ejb.dao.UserDao;
import it.biagio.esc.test.ejb.entity.Location;
import it.biagio.esc.test.ejb.entity.User;

@Path("/user")
public class UserRest {

	@Inject
	UserDao dao;

	/**
	 * Attiva/deattiva uno User
	 */
	@POST
	public Response toggle(User user) {

		User userS = dao.toggle(user);
		JsonObject result = Json.createObjectBuilder().add("id", userS.getId())
				.add("active", userS.getActive())
				.build();
		
		return Response.ok(result).build();
	}

	/**
	 * geolocalizza un utente
	 */
	@GET
	public Response localizza(@Context HttpServletRequest request) {

		Location location1 = new Location();
		location1.setLatitude(Double.valueOf(request.getParameter("N")));
		location1.setLongitudine(Double.valueOf(request.getParameter("E")));

		Location location2 = new Location();
		location2.setLatitude(Double.valueOf(request.getParameter("S")));
		location2.setLongitudine(Double.valueOf(request.getParameter("W")));

		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

		List<User> users =  dao.getUsersByCoordinate(location1, location2);
		for(User user : users){
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
					  .add("firstName", user.getFirstname())
					  .add("lastName", user.getLastname())
					  .add("active", user.getActive());
			jsonArrayBuilder.add(objectBuilder.build());
		}
		
		JsonObject result = Json.createObjectBuilder().add("users", jsonArrayBuilder.build())
				.build();

		return Response.ok(result).build();
	}
}
