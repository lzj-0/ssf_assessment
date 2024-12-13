package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
	NoticeRepository noticeRepository;

	RestTemplate restTemplate = new RestTemplate();

	@Value("${api.url}")
	private String url;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public JsonObject postToNoticeServer(Notice notice) {
		JsonArrayBuilder jArray = Json.createArrayBuilder();
		for (String i : notice.getCategories()) {
			jArray.add(i);
		}

		JsonObject jObj = Json.createObjectBuilder()
								.add("title", notice.getTitle())
								.add("poster", notice.getPoster())
								.add("postDate", notice.getPostDate().getTime())
								.add("categories", jArray)
								.add("text", notice.getText())
								.build();
		// System.out.println(jObj.toString());

		String urlPost = url + "/notice";

		RequestEntity<String> req = RequestEntity.post(urlPost)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.body(jObj.toString());
		try {
			ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
	
			System.out.println(resp.getBody());
	
			JsonReader jReader = Json.createReader(new StringReader(resp.getBody()));
			JsonObject respJson = jReader.readObject();
			String id = respJson.getString("id");
			noticeRepository.insertNotices(id, respJson);
	
			return respJson;
		} catch (HttpStatusCodeException e) {
			// e.printStackTrace();
			return Json.createReader(new StringReader(e.getResponseBodyAsString())).readObject();
		} catch (Exception e) {
			// e.printStackTrace();
			// for other exceptions including jedis connection error
			return Json.createObjectBuilder().add("message", e.getMessage()).build();
		}
		

	}

	public void checkHealth() throws Exception {
		try {
			noticeRepository.getRandomKey();
		} catch (Exception e) {
			throw new Exception();
		}
	}
	

}
