package models;

import java.util.*;
import java.sql.*; //For Timestamp
import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

@Entity
public class Ping extends Model {
	@Id public Long id;
	@Required public Long server_id;
	@Required public Timestamp time;
	@Required public Boolean reachable;

	public static List<Ping> findForServer(Server server){
		return Ping.find.where("server_id = "+server.id).order("time DESC").findList();
	}

	public static Finder<Long,Ping> find = new Finder<Long,Ping>(
		Long.class, Ping.class
	);
}
