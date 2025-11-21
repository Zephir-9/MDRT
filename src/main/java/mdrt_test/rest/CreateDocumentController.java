package mdrt_test.rest;

import mdrt.openapi.api.CreateDocumentApi;
import mdrt.openapi.model.EditResultDTO;
import mdrt_test.service.CreateDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CreateDocumentController implements CreateDocumentApi {
    @Autowired
    CreateDocumentService createDocumentService;

    @Override
    public ResponseEntity<EditResultDTO> addMaster(Integer docId, String docDate, Integer comment) {
        return ResponseEntity.ok(createDocumentService.addMaster(docId, docDate, comment));
    }

    @Override
    public ResponseEntity<EditResultDTO> changeMaster(Integer docId, Integer newDocId, String docDate, Integer comment) {
        return ResponseEntity.ok(createDocumentService.changeMaster(docId, newDocId, docDate, comment));
    }

    @Override
    public ResponseEntity<EditResultDTO> deleteMaster(Integer docId) {
        return ResponseEntity.ok(createDocumentService.deleteMaster(docId));
    }

}
