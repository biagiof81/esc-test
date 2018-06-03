package it.biagio.esc.test.web.application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class EscApplication extends Application {

	/*@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		//resources.add(ObjectMapperProvider.class);
		//resources.add(JacksonFeature.class);
		resources.add(UserRest.class);
		return resources;
	}*/

}
