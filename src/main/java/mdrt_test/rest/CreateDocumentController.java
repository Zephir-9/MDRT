package mdrt_test.rest;

import mdrt.openapi.api.CreateDocumentApi;
import mdrt_test.service.CreateDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CreateDocumentController implements CreateDocumentApi {
    @Autowired
    CreateDocumentService createDocumentService;
}
