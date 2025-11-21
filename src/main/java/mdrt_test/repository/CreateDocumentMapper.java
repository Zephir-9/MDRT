package mdrt_test.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CreateDocumentMapper {
    @Insert("""
            """)
    void addMaster(String docId, String docDate, Integer comment);

    @Update("""
            """)
    void changeMaster(String docId, String newDocId, String docDate, Integer comment);

    @Delete("""
            """)
    void deleteMaster(String docId);

    @Select("""
            select count(*) from docs.master
            where doc_number = #{docId}
            """)
    Integer getDocumentById(String docId);
}
