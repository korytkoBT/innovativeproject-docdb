package controllers;

import java.io.File;
import java.io.IOException;

import model.ClientWebSocket;
import model.ElasticSearchManager;
import model.ElasticSearchServer;
//import model.FileParser;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

import play.*;
import play.libs.Akka;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;
import views.js.SearchScript;

public class Application extends Controller {
    private static String dirPath = "files/";
    private static ElasticSearchServer elasticServer = new ElasticSearchServer();
    private static ElasticSearchManager elasticSearch = new ElasticSearchManager(); 
//    private static FileParser fp = new FileParser();   
    
	public static Result index() {
        Logger.info("service started");
		return ok(index.render("Your new application is ready."));
        
    }
	
    
    public static Result upload(){
    	MultipartFormData body = request().body().asMultipartFormData();
		FilePart uploadedFile = body.getFile("file");
		if (uploadedFile != null) {
			String fileName = uploadedFile.getFilename();
			String contentType = uploadedFile.getContentType(); 
			Logger.info("file received. type:"+contentType);
			File file = uploadedFile.getFile();

			String tmpPath=dirPath + fileName;
			
			try {
				Files.move(file, new File(tmpPath));
				Logger.info("file saved in:"+tmpPath);
			} catch (IOException e) {
				Logger.info("file save failed");
				e.printStackTrace();
			}

//			fp.parseFile(file);

			return ok(index.render("Plik "+fileName+" przesłano na serwer"));
		} else {
			flash("error", "Missing file");
			return redirect(routes.Application.index());    
		}
    }


	public static Result getFile(String path) {
		File file = new File("files/"+path);
		if(file.exists()){
			response().setContentType("application/x-download");
			response().setHeader("Content-disposition","attachment; filename="+path);
			return ok(file);
		}else{
			return redirect(routes.Application.index());   
		}
	}

	//websocket


	public static Result SearchScript() {
		return ok(SearchScript.render());
	}
	
	public static WebSocket<JsonNode> WebSocket() {
		return new WebSocket<JsonNode>() {

			String user = session("connected");
			public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
				
				//Starting webSocket handler
				Akka.system().actorOf(Props.create(ClientWebSocket.class, in, out, user, elasticSearch, elasticServer));
			}

		};
	}
}
