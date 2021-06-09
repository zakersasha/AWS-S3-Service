package com.AWS.s3.controller;

import com.AWS.s3.manager.APIManager;
import com.AWS.s3.models.S3Object;
import com.AWS.s3.repo.S3ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class AWSController {

    @Autowired
    private APIManager apiManager;

    @Autowired
    private S3ObjectRepository s3ObjectRepository;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/s3objects")
    public String listObjects(Model model) {
        apiManager.executeObjects();
        Iterable<S3Object> s3Objects = s3ObjectRepository.findAll();
        model.addAttribute("s3Objects", s3Objects);

        return "s3objects";
    }

    @GetMapping("/s3object/{id}")
    public String splitDetails(@PathVariable(value = "id") long id, Model model) {
        if (!s3ObjectRepository.existsById(id)){
            return "redirect:/s3objects";
        }
        Optional<S3Object> s3Object = s3ObjectRepository.findById(id);
        ArrayList<S3Object> result = new ArrayList<>();
        s3Object.ifPresent(result::add);
        model.addAttribute("s3Objects", result);

        return "s3object-details";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<S3Object> s3Objects;
        if (filter != null && !filter.isEmpty()) {
            s3Objects = s3ObjectRepository.findByName(filter);
        } else {
            s3Objects = s3ObjectRepository.findAll();
        }

        model.put("s3Objects", s3Objects);
        return "s3objects";
    }
}
