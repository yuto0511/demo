package com.example.demo.controller.file.upload;

import com.example.demo.service.file.upload.UploadForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class FileUploadController {

    @RequestMapping(path = "/file/upload", method = RequestMethod.GET)
    public String view(Model model){
        return "upload";
    }

    @RequestMapping(path = "/file/upload", method = RequestMethod.POST)
    public String fileUpload(Model model, UploadForm uploadForm){
        if (uploadForm.getFile().isEmpty()) {
            return "/file/upload";
        }

        Path pathOfFileUpload = Paths.get("C:\\Users\\sugio\\IdeaProjects\\demo\\file\\upload");
        if (!Files.exists(pathOfFileUpload)) {
            try {
                Files.createDirectory(pathOfFileUpload);
            } catch (NoSuchFileException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        String fileExtendion = getFileExtendion(uploadForm);
        String dateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
        Path uploadFilePath = Paths.get(pathOfFileUpload + "\\" + dateTime + fileExtendion);

        try (OutputStream os = Files.newOutputStream(uploadFilePath, StandardOpenOption.CREATE)) {
            byte[] bytes = uploadForm.getFile().getBytes();
            os.write(bytes);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return "redirect:/file/upload";
    }

    private String getFileExtendion(UploadForm uploadForm){
        int placeOfDot = uploadForm.getFile().getOriginalFilename().lastIndexOf(".");
        String fileExtendion = uploadForm.getFile().getOriginalFilename().substring(placeOfDot).toLowerCase();
        return fileExtendion;
    }
}
