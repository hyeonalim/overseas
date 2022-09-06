package sample.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sample.shop.service.FileService;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/imgupload")
    public String index() {
        return "sample/member/imgupload";
    }

    @PostMapping("/imgupload")
    public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        fileService.fileUpload(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "sample/member/imgupload";
    }
}