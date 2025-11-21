package mdrt_test.service;

import mdrt.openapi.model.EditResultDTO;
import mdrt_test.repository.CreateDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentService {
    @Autowired
    CreateDocumentMapper createDocumentMapper;

    public EditResultDTO addMaster(Integer docId, String docDate, Integer comment) {
        if (createDocumentMapper.getDocumentById(docId) > 0) {
            return new EditResultDTO(false, "Документ с номером {" + docId + "} уже существует");
        } else {
            createDocumentMapper.addMaster(docId, docDate, comment);
            return new EditResultDTO(true, "Добавлен документ с номером: " + docId);
        }
    }

    public EditResultDTO changeMaster(Integer docId, Integer newDocId, String docDate, Integer comment) {
        if (docId.equals(newDocId)) {
            createDocumentMapper.changeMaster(docId, newDocId, docDate, comment);
            return new EditResultDTO(true, "Документ с номером: " + docId + " обновлён");
        }
        else if (createDocumentMapper.getDocumentById(newDocId) > 0) {
            return new EditResultDTO(false, "Номер {" + newDocId + "} уже используется другим документом");
        }
        else if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId +"} не существует");
        }
        else {
            createDocumentMapper.changeMaster(docId, newDocId, docDate, comment);
            return new EditResultDTO(true, "Документ с номером: " + docId + " обновлён. Новый номер: " + newDocId);
        }
    }

    public EditResultDTO deleteMaster(Integer docId) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId +"} не существует");
        } else {
            createDocumentMapper.deleteMaster(docId);
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }

}
