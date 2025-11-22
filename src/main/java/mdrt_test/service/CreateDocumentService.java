package mdrt_test.service;

import mdrt.openapi.model.EditResultDTO;
import mdrt_test.repository.CreateDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentService {
    @Autowired
    CreateDocumentMapper createDocumentMapper;

    public EditResultDTO addMaster(String docId, String docDate, Integer comment) {
        if (createDocumentMapper.getDocumentById(docId) > 0) {
            return new EditResultDTO(false, "Документ с номером {" + docId + "} уже существует");
        } else {
            createDocumentMapper.addMaster(docId, docDate, comment);
            return new EditResultDTO(true, "Добавлен документ с номером: " + docId);
        }
    }

    public EditResultDTO changeMaster(String docId, String newDocId, String docDate, Integer comment) {
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

    public EditResultDTO deleteMaster(String docId) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId +"} не существует");
        } else {
            createDocumentMapper.deleteMaster(docId);
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }

    public EditResultDTO addDetail(String docId, String name, Integer ammount) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) > 0) {
            return new EditResultDTO(false, "Спецификация с именем {" + name + "} уже существует");
        } else {
            createDocumentMapper.addDetail(docId, name, ammount);
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }

    public EditResultDTO changeDetail(String docId, String name, String newName, Integer ammount) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) == 0) {
            return new EditResultDTO(false, "Спецификации с именем {" + name + "} не существует в выбранном документе");
        } else {
            createDocumentMapper.changeDetail(docId, name, newName, ammount);
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }

    public EditResultDTO deleteDetail(String docId, String name) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) == 0) {
            return new EditResultDTO(false, "Спецификации с именем {" + name + "} не существует в выбранном документе");
        } else {
            createDocumentMapper.deleteDetail(docId, name);
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }
}
