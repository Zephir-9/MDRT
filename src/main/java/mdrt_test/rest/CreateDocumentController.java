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
    public ResponseEntity<EditResultDTO> addDetail(String docId, String name, Integer ammount) {
        return ResponseEntity.ok(createDocumentService.addDetail(docId, name, ammount));
    }

    @Override
    public ResponseEntity<EditResultDTO> addMaster(String docId, String docDate, Integer comment) {
        return ResponseEntity.ok(createDocumentService.addMaster(docId, docDate, comment));
    }

    @Override
    public ResponseEntity<EditResultDTO> changeDetail(String docId, String name, String newName, Integer ammount) {
        return ResponseEntity.ok(createDocumentService.changeDetail(docId, name, newName, ammount));
    }

    @Override
    public ResponseEntity<EditResultDTO> changeMaster(String docId, String newDocId, String docDate, Integer comment) {
        return ResponseEntity.ok(createDocumentService.changeMaster(docId, newDocId, docDate, comment));
    }

    @Override
    public ResponseEntity<EditResultDTO> deleteDetail(String docId, String name) {
        return ResponseEntity.ok(createDocumentService.deleteDetail(docId, name));
    }

    @Override
    public ResponseEntity<EditResultDTO> deleteMaster(String docId) {
        return ResponseEntity.ok(createDocumentService.deleteMaster(docId));
    }

}
