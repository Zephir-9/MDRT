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
            (#{docId}, #{docDate}, #{comment});
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
            where doc_number = #{docId};
            """)
    void deleteMaster(String docId);

    @Select("""
            select count(*) from docs.master
            where doc_number = #{docId};
            """)
    Integer getDocumentById(String docId);

    @Insert("""
            insert into docs.detail
            (doc_number, item_name, item_amount)
            values
            (#{docId}, #{name}, #{ammount});
            """)
    void addDetail(String docId, String name, Integer ammount);

    @Update("""
            update docs.detail
            set item_name = #{newName}, item_amount = #{ammount}
            where doc_number = #{docId} and item_name = #{name};
            """)
    void changeDetail(String docId, String name, String newName, Integer ammount);

    @Delete("""
            delete from docs.detail
            where doc_number = #{docId} and item_name = #{name};
            """)
    void deleteDetail(String docId, String name);

    @Select("""
            select count(*) from docs.detail
            where doc_number = #{docId} and item_name = #{name};
            """)
    int getSpecificByName(String docId, String name);

    @Insert("""
            INSERT INTO docs.mdrt_log
            (table_name, error_code, message, record_data)
            VALUES
            (#{tableName}, #{errorCode}, #{message}, #{recordData})
            """)
    void documentLog(String tableName, Integer errorCode, String message, String recordData);
}
