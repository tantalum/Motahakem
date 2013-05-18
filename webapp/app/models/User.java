package models;

import java.util.*;
import java.security.*;
import javax.persistence.*;
 
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import java.math.BigInteger;

@Entity
public class User extends Model {

	private static final String SALT = "BOUVIUSDbKJlafkaljhdahfjhLJHFJDhsvab kjabvkjhfda";

	@Id
	@Constraints.Required
	@Formats.NonEmpty
	public String username;

	@Constraints.Required
	public String password;
	
	public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);

	public static List<User> findAll() {
		return find.all();
	}

	public static User findByName(String name) {
		return find.where().eq("username", name).findUnique();
	}

	public static User authenticate(String name, String password) {
		return find.where()
			   .eq("username", name)
			   .eq("password", password)
			   .findUnique();
	}
	
	public static boolean canLogIn(String name){
		return (User.findByName(name) != null);
	}

	public static String hash(String toHash) {
		MessageDigest md;
		String result;
		try {
			md = MessageDigest.getInstance("MD5");
			toHash = toHash + SALT;
			md.update(toHash.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while(result.length() < 32){
				result = "0" + result;
			}
		} catch(NoSuchAlgorithmException  nsae) {
			//NOTICE: Empty catch because this should never ever happen!
			return null;
		}
		return result;
	}
}
