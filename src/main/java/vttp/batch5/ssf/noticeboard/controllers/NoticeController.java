package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Value("${server.port}")
    private Integer port;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String submitForm(@Valid Notice notice, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            return "notice";
        }

        JsonObject respJson = noticeService.postToNoticeServer(notice);
        String id = "";
        try {
            id = respJson.getString("id");
        } catch (Exception e) {
            id = null;
        }
        // System.out.println(id);

        if (id == null) {
            model.addAttribute("error", respJson.getString("message"));
            model.addAttribute("port", port);
            return "fail";
        }

        model.addAttribute("id", id);
        return "success";

    }

    @GetMapping(path = "/status", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> health() {
        try {
            noticeService.checkHealth();
            // System.out.println("Healthy");
            return ResponseEntity.ok().body("{}");
        } catch (Exception e) {
            // System.out.println("Unhealthy");
            return new ResponseEntity<>("{}", HttpStatusCode.valueOf(503));
        }
    }
}
