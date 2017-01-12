package com.goeuro.http;

import com.goeuro.beans.DirectRoute;
import com.goeuro.service.BusRouteService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
//import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class GoEuroHttpServer extends AbstractVerticle {

	BusRouteService busRouteService = new BusRouteService();
	
	@Override
	public void start(Future<Void> fut) {
		
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.route("/").handler(routingContext -> {
			
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html")
			.end("<h1>Hello World</h1>");
			
		});
		
		server.requestHandler(router::accept)
		.listen(
				// Retrieve the port from the configuration, default to 8080
				config().getInteger("http.port", 8080),
				result -> {
		          if (result.succeeded()) {
		            fut.complete();
		          } else {
		            fut.fail(result.cause());
		          }
				}
			);
		
		System.out.println("Port: " + config().getInteger("http.port"));
		
		router.route("/assets/*").handler(StaticHandler.create("assets"));
		
		router.get("/api/direct").handler(this::getDirectRoute);
	}
	
	private void getDirectRoute(RoutingContext routingContext)
	{
		String directRouteJson = "Not available";
		
		String strDepSid = routingContext.request().getParam("dep_sid");
		String strArrSid = routingContext.request().getParam("arr_sid");
		
		if (strDepSid.isEmpty() && strDepSid.isEmpty())
			routingContext.response().setStatusCode(400).end();
		else
		{
			int dep_sid = Integer.valueOf(strDepSid);
			int arr_sid = Integer.valueOf(strArrSid);

			DirectRoute directRoute = busRouteService.getDirectRoute(dep_sid, arr_sid);
		
			if (directRoute != null)
			{
				System.out.println("Source = " + directRoute.getArr_sid());
				System.out.println("Dest = " + directRoute.getArr_sid());	
				
				directRoute.setDirect_bus_route(true);
				directRouteJson = directRoute.toJson4();
			}
			else
			{
				directRoute = new DirectRoute(dep_sid, arr_sid);
				directRoute.setDirect_bus_route(false);
				directRouteJson = directRoute.toJson4();
			}
		}
		
		routingContext.response()
		.putHeader("content-type", "application/json; charset=utf-8")
		.end(directRouteJson);
	}
	
	public void stop(Future<Void> fut) {
		
		busRouteService.destroy();
	}
}
