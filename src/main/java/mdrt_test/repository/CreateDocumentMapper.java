package mdrt_test.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CreateDocumentMapper {
    @Insert("""
            """)
    void addMaster(Integer docId, String docDate, Integer comment);

    @Update("""
            """)
    void changeMaster(Integer docId, Integer newDocId, String docDate, Integer comment);

    @Delete("""
            """)
    void deleteMaster(Integer docId);

    @Select("""
            where id = #{docId}
            """)
    Integer getDocumentById(Integer docId);
}
