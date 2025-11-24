package mdrt_test.rest;

import mdrt.openapi.api.CreateDocumentApi;
import mdrt.openapi.model.DocumentDTO;
import mdrt.openapi.model.EditResultDTO;
import mdrt.openapi.model.LogMDDTO;
import mdrt.openapi.model.SpecificationsDTO;
import mdrt_test.service.CreateDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CreateDocumentController implements CreateDocumentApi {
    @Autowired
    CreateDocumentService createDocumentService;

    @Override
    public ResponseEntity<EditResultDTO> addDetail(String docId, String name, Integer ammount) {
        return ResponseEntity.ok(createDocumentService.addDetail(docId, name, ammount));
    }

    @Override
    public ResponseEntity<EditResultDTO> addMaster(String docId, LocalDate docDate, String comment) {
        return ResponseEntity.ok(createDocumentService.addMaster(docId, docDate, comment));
    }

    @Override
    public ResponseEntity<EditResultDTO> changeDetail(String docId, String name, String newName, Integer ammount) {
        return ResponseEntity.ok(createDocumentService.changeDetail(docId, name, newName, ammount));
    }

    @Override
    public ResponseEntity<EditResultDTO> changeMaster(String docId, String newDocId, LocalDate docDate, String comment) {
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

    @Override
    public ResponseEntity<List<SpecificationsDTO>> getDetail(String docId) {
        return ResponseEntity.ok(createDocumentService.getDetail(docId));
    }

    @Override
    public ResponseEntity<List<LogMDDTO>> getLog() {
        return ResponseEntity.ok(createDocumentService.getLog());
    }

    @Override
    public ResponseEntity<List<DocumentDTO>> getMaster() {
        return ResponseEntity.ok(createDocumentService.getMaster());
    }

}
