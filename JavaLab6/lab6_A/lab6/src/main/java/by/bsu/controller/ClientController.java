package by.bsu.controller;

import by.bsu.entities.Client;
import by.bsu.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository ClientRepository;

    @GetMapping("/list")
    public String listClient(Model model) {
        List<Client> Clients = ClientRepository.findAll();
        model.addAttribute("clients", Clients);
        return "/client/list";
    }
}