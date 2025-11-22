package mdrt_test.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CreateDocumentMapper {
    @Insert("""
            insert into docs.master
            (doc_number, doc_date, comment)
            values
            (#{docId}, #{docDate}, #{comment})
            """)
    void addMaster(String docId, String docDate, Integer comment);

    @Update("""
            update docs.master
            set doc_number = #{newDocId}, doc_date = #{docDate}, comment = #{comment}
            where doc_number = #{docId};
            """)
    void changeMaster(String docId, String newDocId, String docDate, Integer comment);

    @Delete("""
            delete from docs.master
            where doc_number = #{docId}
            """)
    void deleteMaster(String docId);

    @Select("""
            select count(*) from docs.master
            where doc_number = #{docId}
            """)
    Integer getDocumentById(String docId);
}
