package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;
import models.*;

public class Application extends Controller {
 	
	public static class Login {
		public String username;
		public String password;

		public String validate() {
			String passwordHash = User.hash(password);
			if(User.authenticate(username, passwordHash) == null) {
				return "Invalid user name or password";
			}
			return null;
		}
	}
			
 
	public static Result index() {
		if(Secured.isLoggedin()){
			return redirect(routes.Servers.servers());
		} else {
			return redirect(routes.Application.login());
		}
	}

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	public static Result authenticate() {
		Form<Login> loginForm = new Form(Login.class).bindFromRequest();
		if(loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("username", loginForm.get().username);
			return redirect(
				routes.Servers.servers()
			);
		}
	}

	public static Result logout() {
		session().clear();
		flash("success", "You have been logged out");
		return redirect(
			routes.Application.login()
		);

	}

  
}
