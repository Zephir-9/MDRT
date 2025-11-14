package mdrt_test.service;

import mdrt_test.repository.CreateDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentService {
    @Autowired
    CreateDocumentMapper createDocumentMapper;
}
