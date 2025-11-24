package mdrt_test.service;

import mdrt.openapi.model.EditResultDTO;
import mdrt_test.repository.CreateDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CreateDocumentService {
    @Autowired
    CreateDocumentMapper createDocumentMapper;

    public EditResultDTO addMaster(String docId, LocalDate docDate, String comment) {
        if (createDocumentMapper.getDocumentById(docId) > 0) {
            createDocumentMapper.documentLog("Master", 1, "Попытка создания дупликата",
                    "{Номер документа: " + docId + ", Дата документа: " + docDate + ", Примечание: " + comment + "}");
            return new EditResultDTO(false, "Документ с номером {" + docId + "} уже существует");
        } else {
            createDocumentMapper.addMaster(docId, docDate, comment);
            createDocumentMapper.documentLog("Master", 0, "Успешное создание документа",
                    "{Номер документа: " + docId + ", Дата документа: " + docDate + ", Примечание: " + comment + "}");
            return new EditResultDTO(true, "Добавлен документ с номером: " + docId);
        }
    }

    public EditResultDTO changeMaster(String docId, String newDocId, LocalDate docDate, String comment) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            createDocumentMapper.changeMaster(docId, newDocId, docDate, comment);
            createDocumentMapper.documentLog("Master", 1, "Документ ещё не создан",
                    "{Номер документа: " + docId + ", Дата документа: " + docDate + ", Примечание: " + comment + "}");
            return new EditResultDTO(false, "Документа с номером {" + docId +"} не существует");
        }
        else if (createDocumentMapper.getDocumentById(newDocId) > 0 && !docId.equals(newDocId)) {
            createDocumentMapper.documentLog("Master", 1, "Попытка дублирования номера",
                    "{Номер документа: " + docId + ", Дата документа: " + docDate + ", Примечание: " + comment + "}");
            return new EditResultDTO(false, "Номер {" + newDocId + "} уже используется другим документом");
        }
        else {
            createDocumentMapper.changeMaster(docId, newDocId, docDate, comment);
            createDocumentMapper.documentLog("Master", 0, "Документ успешно изменён",
                    "{Номер документа: " + docId + ", Дата документа: " + docDate + ", Примечание: " + comment + "}");
            return new EditResultDTO(true, "Документ с номером: " + docId + " обновлён. Новый номер: " + newDocId);
        }
    }

    public EditResultDTO deleteMaster(String docId) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            createDocumentMapper.documentLog("Master", 1, "Документ ещё не создан",
                    "{Номер документа: " + docId + "}");
            return new EditResultDTO(false, "Документа с номером {" + docId +"} не существует");
        } else {
            createDocumentMapper.deleteMaster(docId);
            createDocumentMapper.documentLog("Master", 0, "Документ удалён",
                    "{Номер документа: " + docId + "}");
            return new EditResultDTO(true, "Документ с номером: " + docId + " удалён");
        }
    }

    public EditResultDTO addDetail(String docId, String name, Integer ammount) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            createDocumentMapper.documentLog("Detail", 1, "Документ ещё не создан",
                    "{Номер документа: " + docId + "}");
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) > 0) {
            createDocumentMapper.documentLog("Detail", 1, "Попытка дублирования спецификации",
                    "{Имя спецификации: " + name + ", Номер документа: " + docId + "}");
            return new EditResultDTO(false, "Спецификация с именем {" + name + "} уже существует");
        } else {
            createDocumentMapper.addDetail(docId, name, ammount);
            createDocumentMapper.documentLog("Detail", 0, "Спецификация успешно создана",
                    "{Имя спецификации: " + name + ", Номер документа: " + docId + ", Сумма  спецификации: " + ammount + "}");
            return new EditResultDTO(true, "Спецификация {" + name + "} документа {" + docId + "} успешно добавлена");
        }
    }

    public EditResultDTO changeDetail(String docId, String name, String newName, Integer ammount) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            createDocumentMapper.documentLog("Detail", 1, "Документ ещё не создан",
                    "{Номер документа: " + docId + "}");
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) == 0) {
            createDocumentMapper.documentLog("Detail", 1, "Спецификация ещё не создана",
                    "{Номер документа: " + docId + ", Имя спецификации: " + name + "}");
            return new EditResultDTO(false, "Спецификации с именем {" + name + "} не существует в выбранном документе");
        } else {
            createDocumentMapper.changeDetail(docId, name, newName, ammount);
            createDocumentMapper.documentLog("Detail", 0, "Спецификация успешно изменена",
                    "{Номер документа: " + docId + ", Старое имя спецификации: " + name + ", Новое имя: " + newName + ", Новая сумма: " + ammount + "}");
            return new EditResultDTO(true, "Спецификация с номером: " + docId + " изменена. " +
                    "Название: " + newName + ", сумма: " + ammount);
        }
    }

    public EditResultDTO deleteDetail(String docId, String name) {
        if (createDocumentMapper.getDocumentById(docId) == 0) {
            createDocumentMapper.documentLog("Detail", 1, "Документ ещё не создан",
                    "{Номер документа: " + docId + "}");
            return new EditResultDTO(false, "Документа с номером {" + docId + "} не существует");
        } else if (createDocumentMapper.getSpecificByName(docId, name) == 0) {
            createDocumentMapper.documentLog("Detail", 1, "Спецификация ещё не создана",
                    "{Номер документа: " + docId + ", Имя спецификации: " + name + "}");
            return new EditResultDTO(false, "Спецификации с именем {" + name + "} не существует в выбранном документе");
        } else {
            createDocumentMapper.deleteDetail(docId, name);
            createDocumentMapper.documentLog("Detail", 0, "Спецификация успешно удалена",
                    "{Номер документа: " + docId + ", Имя спецификации: " + name + "}");
            return new EditResultDTO(true, "Спецификация документа {" + docId + "} с именем {" + name + "} удалена");
        }
    }
}
