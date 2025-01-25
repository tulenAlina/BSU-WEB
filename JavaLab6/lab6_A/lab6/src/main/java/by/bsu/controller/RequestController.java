package by.bsu.controller;

import by.bsu.entities.Request;
import by.bsu.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestRepository RequestRepository;

    @GetMapping("/list")
    public String listRequest(Model model) {
        List<Request> Requests = RequestRepository.findAll();
        model.addAttribute("requests", Requests);
        return "/request/list";
    }
}